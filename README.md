Creating a ZIP with directory contents
====

This example uses java.util.zip.* to create a zip file with directory contents.

*  Set the directory and zip filename and this will create the zip file
*  You can avoid overwriting an existing zip file, or replace it
*  You can return the zip filename, or the zip contents as FileInputStream

After the file is created, another flow with a File inbound endpoint will take the file and upload it via FTP

Required Properties
----
*  zipDirectory: directory with files
*  zipFile: zip file name

Optional Properties
----

*  zipFileOverwrite: if not set and a zip file already exists, it will throw an exception. If set to "true" it will overwrite the file
*  zipReturnContent: if not set, the payload will be set to the zip file name. If set to "true" the payload will be a FileInputStream (the zip file)

Example
----
```
<set-variable variableName="zipDirectory" value="#['/Users/matiasjuan/Downloads/devel/temp/tyrone']" doc:name="zipDirectory" />
<set-variable variableName="zipFile" value="#['/Users/matiasjuan/Downloads/devel/temp/myZip.zip']" doc:name="zipFile" />
<set-variable variableName="zipFileOverwrite" value="true" doc:name="zipFileOverwrite" />
        
<component class="com.mulesoft.se.demos.ZIPDir" doc:name="Java"/>
```




