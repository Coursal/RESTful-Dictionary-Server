# RESTful-Dictionary-Server
An example of a REST web service server that checks a user's credentials and returns a number of words from a dictionary.

### Web services
* **Login** where the credentials of the user are being checked within a database to return an authentication token. A 90 second timer begins until the expiration of the token.

* **Words** where based on a token and two letters returns a list of words from a dictionary that begin with the first given letter and end with the other one.

All data are being sent and received with the **JSON** format.

### Testing from browser
`localhost:8080/web_services/webresources/web_services/stanley&valve`

`localhost:8080/web_services/webresources/web_services?token=BxHJSLP4qLauphWaGbeLEueNBs575s&first_letter=a&last_letter=r`

Tied with sister project [RESTful-Dictionary-Client](https://github.com/Coursal/RESTful-Dictionary-Client).

Server written and tested in NetBeans 8.2 (requires JDBC library for the SQL script and JSON.simple jar file, both added in the project directory).

Database tested in XAMPP.
