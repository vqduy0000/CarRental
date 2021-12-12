use car_rental;
-- ('', '', '', '', '', '', '', '', '', '', '', '', '' ,'' ,'', '')
insert into PERSON (PERSON_LNAME, PERSON_FNAME, PERSON_MNAME, 
PERSON_STREET, PERSON_CITY, PERSON_STATE, PERSON_ZIPCODE, 
PERSON_DOB, PERSON_AREACODE, PERSON_PHONE, PERSON_USERNAME, 
PERSON_PASSWORD, PERSON_EMAILID, PERSON_EMAILDOMAIN, 
PERSON_GENDER, PERSON_TYPE) 
values
	('John', 'Doe', null, '12345 1st Street', 'Detroit', 'MI', 48127, '1999-09-11', '313', '4356969', 'jdoe99', '123456', 'jdoe99' ,'gmail.com' ,'M', 'E'),
    ('Jane', 'Doe', 'Joe', '33333 nth Street', 'Houston', 'TX', 77001, '1989-01-17', '281', '6668878', 'jdoe89', 'badpassword', 'jdoe89' ,'yahoo.com' ,'F', 'C'),
    ('Jonathan', 'Wright', 'C', '123 Elm Street', 'Detroit', 'MI', 48201, '1977-12-11', '313', '8779001', 'jonawr1', 'bestpassword', 'jona1977' ,'hotmail.com' ,'M', 'C'),
    ('Oliber', 'Bulberry', null, '445 Real Street', 'Detroit', 'MI', 48202, '1967-04-11', '313', '6768888', 'oblibee11', 'insercure', 'oblibee' ,'gmail.com' ,'M', 'C'),
    ('Olivia', 'Donnavan', null, '707 7th Street', 'Detroit', 'MI', 48205, '1990-01-01', '313', '3139919', 'donnaliv', 'R1ghtpa55w0rd', 'thedonna90' ,'gmail.com' ,'F', 'C');

insert into OFFICE (OFFICE_ID, OFFICE_STREET, OFFICE_CITY, OFFICE_STATE, OFFICE_ZIPCODE, OFFICE_AREACODE, OFFICE_PHONE) 
values 
	('01', '98701 Major Street', 'Detroit', 'MI', '48222', '313', '9897777'),
    ('02', '89111 New York Street', 'New York City', 'NY', '10009', '212', '9900099');
    
insert into CUSTOMER (PERSON_ID, OFFICE_ID)
values
	('2', '01');
    
insert into CAR (OFFICE_ID, CAR_BRAND, CAR_YEAR, CAR_COLOR, 
CAR_FUEL_EFFICIENCY, CAR_BODYSTYLE, CAR_DRIVETYPE, CAR_TRANSMISSION, 
CAR_ENGINE, CAR_TRIM, CAR_MILEAGE, CAR_RENT, CAR_CONDITION, CAR_AVAILABILITY, PERSON_ID) 
values 
	('01', 'Toyota', '2020', 'black', '39', 'Minivan', 'AWD', 'A', 'v6', '1', '10910', '500', 'G', '0', '2'),
    ('01', 'Ford', '2021', 'black', '50', 'Pick up', 'AWD', 'M', 'v6', '1', '5125', '700', 'G', '1', null),
    ('01', 'Tesla', '2020', 'white', '10', 'Sedan', 'FWD', 'A', 'electric', '30718', '10910', '300', 'G', '0', null);


