DROP TABLE IF EXISTS CARS;

DROP TABLE IF EXISTS PERSONS;

drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start with 1 increment by 1;

CREATE TABLE CARS (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  type VARCHAR(250) NOT NULL,
  PERSON_ID INT,
);

CREATE TABLE PERSONS(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
);