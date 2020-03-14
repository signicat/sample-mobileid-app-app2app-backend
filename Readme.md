Sample MobileID App App2App Backend
=====================================

This is a simple sample app that shows how Signicat's mobile app can be used together with the Merchant's mobile app and merchant's web solution.
The sample backend server uses the OIDC protocol for communication with Signicat.


Configuration
----------------------------------
-   This project reads the following application configuration file

        src/main/resources/application.yaml

-   The "oidc" section of this file contains properties that have to be changed prior to execution:
	-   The "redirectUrl" parameter is the callback URL that Signicat's server will use to post results to, once operation (reg or auth) is carried out. 
	    Therefore, this server must be publicly accessible.
	    Also, this redirect_uri has to be configured on Signicat's side, for the particular OIDC client you use.
         

Build and execute
----------------------------------
In order to use this Sample code, first build the project

        mvn clean install

Then, run it
    
        java -jar target/sample-mobileid-app-app2app-backend.jar
        
If you have not changed the "port" property in the "server" section of the application.yaml file, the server can be accessed at 

        http://localhost:8089


