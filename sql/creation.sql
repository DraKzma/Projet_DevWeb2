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