DROP DATABASE IF EXISTS users_db;
CREATE DATABASE users_db DEFAULT CHARACTER SET Utf8 COLLATE Utf8_general_ci;

DROP USER IF EXISTS 'admin'@'localhost';
GRANT ALL PRIVILEGES ON users_db.* TO 'admin'@'localhost' IDENTIFIED BY 'admin';

USE users_db;

CREATE TABLE users
(
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
	PRIMARY KEY (username)
);
    
INSERT INTO users VALUES("hit", "vibes");
INSERT INTO users VALUES("miles", "m1135");
INSERT INTO users VALUES("stanley", "valve");

CREATE TABLE dictionary
(
	word VARCHAR(50) NOT NULL
);

SELECT * FROM users;