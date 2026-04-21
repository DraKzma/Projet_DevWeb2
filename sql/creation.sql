CREATE DATABASE IF NOT EXISTS Projet_DevWeb2;

USE Projet_DevWeb2;

CREATE USER IF NOT EXISTS webuser@localhost
IDENTIFIED BY 'multipass';

GRANT ALL
ON Projet_DevWeb2.*
TO webuser@localhost;

CREATE TABLE IF NOT EXISTS users(
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(64) UNIQUE NOT NULL,
	password VARCHAR(64) NOT NULL,
	permissions INT NOT NULL
);

CREATE TABLE IF NOT EXISTS documents(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS hasAccessTo(
	user_id INT NOT NULL,
	document_id INT NOT NULL,
	role ENUM('VIEWER', 'WRITER', 'OWNER') NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (document_id) REFERENCES documents(id),
	PRIMARY KEY (user_id, document_id)
);