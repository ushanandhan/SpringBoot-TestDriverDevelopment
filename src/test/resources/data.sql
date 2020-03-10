DROP TABLE IF EXISTS CARS;

CREATE TABLE CARS (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  type VARCHAR(250) NOT NULL
);

INSERT INTO CARS (id, name, type) VALUES ('1001','Duster','hybrid');
INSERT INTO CARS (id, name, type) VALUES ('1002','Micra','hatchback');
INSERT INTO CARS (id, name, type) VALUES ('1003','Lodgy','suv');