package com.mulesoft.se.demos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.cli.MissingArgumentException;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class ZIPDir implements Callable {
	public static final String DIRECTORY = "zipDirectory";
	public static final String ZIPFILE = "zipFile";
	public static final String ZIPFILEOVERWRITE = "zipFileOverwrite";
	public static final String ZIPCONTENT = "zipReturnContent";
	

	@Override
	public Object onCall(MuleEventContext eventContext) throws MissingArgumentException, IOException  {
		if (eventContext.getMessage().getInvocationProperty(DIRECTORY)==null)
			throw new MissingArgumentException(String.format("Invocation property expected '%s'", DIRECTORY));
		if ( !(eventContext.getMessage().getInvocationProperty(DIRECTORY) instanceof String))
			throw new MissingArgumentException(String.format("Invocation property '%s' is wrong type", DIRECTORY));

		if (eventContext.getMessage().getInvocationProperty(ZIPFILE)==null)
			throw new MissingArgumentException(String.format("Invocation property expected '%s'", ZIPFILE));
		if ( !(eventContext.getMessage().getInvocationProperty(ZIPFILE) instanceof String))
			throw new MissingArgumentException(String.format("Invocation property '%s' is wrong type", ZIPFILE));


		String zipDirectoryName = (String)eventContext.getMessage().getInvocationProperty(DIRECTORY);
		String zipFileName = (String)eventContext.getMessage().getInvocationProperty(ZIPFILE);

		File zipDirectory = new File(zipDirectoryName);
		if(!zipDirectory.exists())
			throw new IOException(String.format("Directory '%s' does not exist", zipDirectoryName));

		File zipFile = new File(zipFileName);
		
		boolean replaceExisting = 
				"true".equals(eventContext.getMessage().getInvocationProperty(ZIPFILEOVERWRITE)) ||
				"TRUE".equals(eventContext.getMessage().getInvocationProperty(ZIPFILEOVERWRITE)) ||
				"1".equals(eventContext.getMessage().getInvocationProperty(ZIPFILEOVERWRITE));
		
		if(zipFile.exists() && !replaceExisting )
			throw new IOException(String.format("File '%s' already exist", zipFileName));

		//start the zipping
		zip(zipFile, zipDirectory);

		boolean returnContents = 
				"true".equals(eventContext.getMessage().getInvocationProperty(ZIPCONTENT)) ||
				"TRUE".equals(eventContext.getMessage().getInvocationProperty(ZIPCONTENT)) ||
				"1".equals(eventContext.getMessage().getInvocationProperty(ZIPCONTENT));
		
		if ( returnContents ) {
			FileInputStream fis = new FileInputStream(zipFile);
			return fis ;
		}
		
		return zipFileName ;
	}
	

	public void zip(File zipFile, File zipDirectory) throws IOException {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			File[] listOfFiles = zipDirectory.listFiles(); 

			for (File file : listOfFiles) {
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					byte[] byteBuffer = new byte[1024];
					int bytesRead = -1;
					while ((bytesRead = fis.read(byteBuffer)) != -1) {
						zos.write(byteBuffer, 0, bytesRead);
					}
					zos.flush();
				} finally {
					try {
						fis.close();
					} catch (Exception e) {
					}
				}

			}

			zos.closeEntry();

			zos.flush();
		} finally {
			try {
				zos.close();
			} catch (Exception e) {
			}
		}
	}


}
