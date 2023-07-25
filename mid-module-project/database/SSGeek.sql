-- Database: SSGeek
--
-- Contains tables for viewing products on the Solar System Geek site, 
-- and for placing an order.

DROP TABLE IF EXISTS line_item;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
	product_id serial,
	name varchar(128) NOT NULL,
	description varchar NOT NULL,
	price decimal (10,2) NOT NULL,
	image_name varchar(256),
	CONSTRAINT PK_product PRIMARY KEY (product_id)
);

CREATE TABLE customer (
	customer_id serial,
	name varchar(128) NOT NULL,
	street_address1 varchar(128) NOT NULL,
	street_address2 varchar(128),
	city varchar(64) NOT NULL,
	state char(2) NOT NULL,
	zip_code char(5) NOT NULL,
	CONSTRAINT PK_customer PRIMARY KEY (customer_id)
);

-- A bill-of-sale (sales order)
CREATE TABLE sale (
	sale_id serial,
	customer_id INT NOT NULL,
	sale_date date NOT NULL,
	ship_date date,
	CONSTRAINT PK_sale PRIMARY KEY (sale_id),
	CONSTRAINT FK_sale_customer FOREIGN KEY(customer_id) REFERENCES customer(customer_id)
);

-- A single line of a bill-of-sale
CREATE TABLE line_item (
	line_item_id serial,
	sale_id INT NOT NULL,
	product_id INT NOT NULL,
	quantity INT NOT NULL,
	CONSTRAINT PK_line_item PRIMARY KEY (line_item_id),
	CONSTRAINT FK_line_item_sale FOREIGN KEY(sale_id) REFERENCES sale(sale_id),
	CONSTRAINT FK_line_item_product FOREIGN KEY(product_id) REFERENCES product(product_id)
);

INSERT INTO product(name, description, price, image_name) VALUES
-- 1
 ('Coffee Mug', 'Staying up late to take in the wonders of the solar system can make a geek a little sluggish in the morning. This awesome mug is just what you need to perk up in the morning with your caffeinatened beverage of choice!', 9.99, 'ssg_mug.png' ),
-- 2
 ('SOLAR SYSTEM: A Visual Exploration of the Planets, Moons, and Other Heavenly Bodies that Orbit Our Sun', 'This beautiful book presents a new and fascinating way to experience our planetary neighborhood. With hundreds of stunning photographs and graphics, as well as fascinating text by the award-winning writer and broadcaster, Marcus Chown, Solar System takes us on a whirlwind tour of the planets, dwarf planets, moons and asteroids that orbit our sun.', 29.95, 'solarsystem_book.png' ),
-- 3
 ('Geek T-Shirt', 'Get your geek on (literally!) in this stylish T-Shirt! 100% cotton, pre-shrunk, one size fits some.', 19.99, 'geek_tshirt.png' ),
-- 4
 ('Celestial Buddies Sun Moon Earth Plush Set with Solar System Chart', 'Celestial Buddies is an original line of plush characters each personifying a celestial body occupying our heavens. Each character comes with a tag showing the actual object it personifies and some fun facts to give the toy educational value. This collection contains the 3 celestial buddies we are most aware of - the sun (9 inches), moon (5 inches) and Earth (6 inches). Also included is a chart of the Solar System, to help your child identify where the buddies are located in our universe.', 72.78, 'planet_toys.png' ),
-- 5
 ('Midnight Planetarium Watch', 'The planets evolve at their real speeds of orbit, while a graceful star indicates your lucky day. A poetic invitation to immortalize a special date.', 221999.99, 'watch-1.png' );

INSERT INTO customer (name, street_address1, street_address2, city, state, zip_code) VALUES
-- 1
 ('Sherlock Holmes', '221B Baker Street', 'Apartment B', 'London', 'OH', 43140),
-- 2
 ('Mona Lisa', '99 rue de Rivoli', null, 'Paris', 'OH', 45347),
-- 3
 ('Lady Liberty', 'Liberty Island', null, 'New York', 'NY', 10004),
-- 4
 ('The President', '1600 Pennsylvania Avenue NW', null, 'Washington', 'DC', 20500),
-- 5
 ('Anne Frank', '263 Prinsengracht', null, 'Amsterdam', 'OH', 43903),
-- 6
 ('Elwood Blues', '1060 West Addison Street', null, 'Chicago', 'IL', 60613);
 
INSERT INTO sale (customer_id, sale_date, ship_date) VALUES
-- 1 
  (1, '2022-01-01', '2022-01-04'),
-- 2
  (1, '2022-02-01', '2022-02-03'),
-- 3
  (1, '2022-05-15', null),
-- 4
  (2, '2022-02-01', '2022-02-06'),
-- 5 
  (2, '2022-02-28', '2022-03-04'),
-- 6
  (3, '2022-06-01', null),
-- 7 
  (3, '2022-06-02', '2022-06-03'),
-- 8
  (3, '2022-06-02', null);

INSERT INTO line_item (sale_id, product_id, quantity) VALUES
-- 1
  (1, 1, 10),
-- 2
  (1, 2, 1),
-- 3
  (1, 4, 4),
-- 4
  (2, 3, 5),
-- 5
  (2, 4, 4),
-- 6
  (3, 1, 4),
-- 7
  (3, 2, 5),
-- 8
  (3, 3, 1),
-- 9
  (3, 4, 3),
-- 10
  (4, 2, 2),
-- 11
  (5, 1, 10),
-- 12
  (5, 2, 1),
-- 13
  (5, 4, 4),
-- 14
  (6, 3, 5),
-- 15
  (6, 4, 4),
-- 16
  (7, 1, 4),
-- 17
  (7, 2, 5),
-- 18
  (7, 3, 1),
-- 19
  (7, 4, 3),
-- 20
  (8, 2, 2);
  
