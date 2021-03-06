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
-------------------------------

This project reads an application configuration file (default profile: application.yaml)

In order to use this sample code, first build the project:

        mvn clean package

Then, run it:
    
        java -jar target/sample-mobileid-app-app2app-backend.jar
        
    or  with 
    
        java -jar -Dspring.profiles.active=<profile> target/sample-mobileid-app-app2app-backend.jar
        
If you have not changed the "port" property in the "server" section of the application.yaml file, the server can be accessed at 

        http://localhost:8089
        
PS : As part of the above build process, our Maven plugins will 

        - Install node/npm in 'src/frontend'
        - Install node packages
        - Compile and minify frontend components for production distribution
        - Copy the frontend components to the 'main/resource/public' directory to launch the application UI on start-up of
         this Springboot application
        
        
# MobileID Sample App frontend 

## Frontend project setup (You only need these if you want to modify the UI for this project)
PS : Please go to 'src/frontend' in this project directory 

### Install npm
```
npm install
```

### Compile and hot-reload for development
```
npm run serve
```

### Compile and minify for production
```
npm run build
```
