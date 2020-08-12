# university-appointment
Web platform for students and lecturers.
 
# Technologies
- Java 8
- Spring Boot 2.2.9 RELEASE
- Spring(Data, Security, Web)
- Lombok
- PostgreSQL 10.6
- Mapstruct
- Flyway
 
# Requirements 
* You should have JDK 8 and Maven installed
* PostgreSQL
# Steps To run the application
- Download the project.
- Open application.properties and set values for your Database:
 
spring.datasource.url=@Data Base URL@
 
spring.datasource.username=@UserName@
 
spring.datasource.password=@Password@
 
- For the email notifications to work you have to set your email account values in the application.properties: 
spring.mail.username = @email@
spring.mail.password = @password@
 
- Now you have to let sending emails from less secure apps such as this one. For this, you need to visit: https://www.google.com/settings/security/lesssecureapps and enable “Allow less secure applications”(if you are using Gmail). For other email providers? Please contact me for help.
 
- Open command prompt at pom.xml level and type "mvn package" command.
- In the target folder, you can see the university-appointment-0.0.1-SNAPSHOT.jar іs generated.
- Use the command "java -jar ./target/university-appointment-0.0.1-SNAPSHOT.jar" to run the jar file.
- Now the application has started and Flyway will generate the Database tables and some input values.(In case you get errors from Flyway, try deleting and again creating your database)
 
- Making the browser trust the SSL certificate(This is only for testing. If you're going to deploy the Application to a real server, please create an SSL Certificate using Cloudflare or use the one that is proved by your host).
I suggest you check the official guide on how to import a PKCS12 file into your specific client. On macOS, for example, you can directly import a certificate into the Keychain Access (which browsers like Safari, Chrome, and Opera rely on to manage certificates).
If deploying the application on localhost, you may need to do a further step from your browser: enabling insecure connections with localhost.
In Firefox, you are shown an alert message. To access the application, you need to explicitly define an exception for it and make Firefox trust the certificate.
In Chrome, you can write the following URL in the search bar: chrome://flags/#allow-insecure-localhost and activate the relative option.
 
- All needed information about the endpoints and the fields that should be provided will be available by the link https://localhost:8443/swagger-ui.html.
 
 All further instructions suppose that you will be using Postman and you know what you are doing.
 
- After all, this is done, hit http://localhost:8443/user/register URL on the browser to register a new user(please use real email). 
 
- You will get an email with a link to confirm your account. Click on the URL to do that.
 
* You can skip the step above by just enabling the user in the database.
 
- Now go to http://localhost:8443/authenticate and you will get a JWT in response which you will use as the authentication for every subsequent request.
