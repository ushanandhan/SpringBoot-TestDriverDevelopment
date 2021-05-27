DROP TABLE IF EXISTS CARS;

drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start with 1 increment by 1;

CREATE TABLE CARS (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  type VARCHAR(250) NOT NULL
);