<!---
Links used in this doc.  Putting them in link id's for easy maintenance
-->
[moustache]: https://github.com/dskyberg-Ping/moustache
[maven]: http://maven.apache.org
[apache-v2]: http://www.apache.org/licenses/LICENSE-2.0.html

<!---
The actual doc
-->
PingOne Demo App
===================
**PingOne-App** is an unsupported demo application that shows how to set up a SaaS application with PingOne.  
Again - this demo is not supported in any way by Ping Identity.

##Installation

1. Install the pre-reqs (Java JDK 7, Git, and Maven)
1. Use Git to download Moustache and PingOneApp
1. Compile and install Moustache
1. Update template properties
1. Use Maven jetty:run to launch the app.

### Install the pre-reqs (Java JDK 7, Git, and Maven)
**PingOneApp** requires Java JDK 7.  I haven't tested with OpenJDK.  Oracle JDK works.

Source is all in GitHub.  You will need git installed on your system.

This app uses [Maven v3+][maven].  Install it.  Use it.  Love it.

### Use Git to download Moustache and PingOneApp
Figure out where you want to install the software.  Go there.  Run the following

	git clone https://github.com/dskyberg-Ping/moustache.git
	git clone https://github.com/dskyberg-Ping/pingone-app.git

### Compile and install Moustache
**PingOne-App** leverages [moustache][moustache] to compile config files from a configuration template.  
Go to the moustache folder that was generated by git.  Run the following:

	mvn install

This will ask Maven to compile, test and publish moustache to your local Maven repository (typically `<home>/.m2/repository`).
Once moustache is published locally, it will be available for PingOneApp.

### Update template properties
Generating the various resource files from the templates is a two step process.

Edit the file `templates/template.properties`.  Don't change anything in `${}` syncax.  Maven will
update these with appropriate values based on your installation. 

Note: You can use any legitimate Maven variable in the template.properties file, and Maven will
generate appropriate values during the `process-resources` build phase.

### Use Maven jetty:run to launch the app
You are now ready to launch your brand new app!  Run the following from the PingOneApp folder:

`mvn jetty:run`

Jetty is now hosting your app on port 8443

Note - if you already 
## How it works

####template.properties
Use a text editor to open templates/template.properties.    


###Setting up the Database:
**PingOne-App** uses Hypersonic HSQLDB. To create the database, run the following:
java -cp <app install dir>/WEB-INF/lib/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
Use the following parameters:
-- Type: HSQLDB Database Engine Standalone
-- Driver: org.hsqldb.jdbcDriver
-- URL: <database.url from app.properties>
-- User: <database.user from app.properties> 
-- Password: <database.password from app.properties>

##License
Licensed under [Apache License, Version 2.0][apache-v2]
