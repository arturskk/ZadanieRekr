DROP TABLE IF EXISTS customerdata;
DROP TABLE IF EXISTS customernote;

CREATE TABLE customerdata (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  info_as_of_date DATE NOT NULL,
  customer_name VARCHAR(150) NOT NULL,
  customer_id INT NOT NULL,
  customer_start_date DATE DEFAULT NULL,
  customer_type VARCHAR(20) DEFAULT NULL,
  customer_income DECIMAL DEFAULT NULL,
  customer_risk_class VARCHAR(20) DEFAULT NULL,
  customer_business_type VARCHAR(20) DEFAULT NULL,
  r1 DECIMAL DEFAULT NULL,
  r2 DECIMAL DEFAULT NULL
);

CREATE TABLE customernote (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  note_type VARCHAR(250) DEFAULT NULL,
  title VARCHAR(250) DEFAULT NULL,
  content VARCHAR(250) DEFAULT NULL,
  note_day DATE NOT NULL,
  customer_id INT NOT NULL
);


  