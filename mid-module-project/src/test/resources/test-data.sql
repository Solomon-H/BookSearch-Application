BEGIN TRANSACTION;

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

-- Insert test data

-- Product
INSERT INTO product(name, description, price, image_name)
VALUES
 ('Product 1', 'Description 1',   9.99, 'product-1.png' ),  -- id=1
 ('Product 2', 'Description 2',  19.00, 'product-2.png' ),  -- id=2
 ('Product 3', 'Description 3', 123.45, 'product-3.png' ),  -- id=3
 ('Product 4', 'Description 4',   0.99, 'product-4.png' );  -- id=4

-- Customer
INSERT INTO customer (name, street_address1, street_address2, city, state, zip_code)
VALUES
 ('Customer 1', 'Addr 1-1', 'Addr 1-2', 'City 1', 'S1', 11111),    -- id=1
 ('Customer 2', 'Addr 2-1', 'Addr 2-2', 'City 2', 'S2', 22222),    -- id=2
 ('Customer 3', 'Addr 3-1',       null, 'City 3', 'S3', 33333),    -- id=3
 ('Customer 4', 'Addr 4-1',       null, 'City 4', 'S4', 44444);    -- id=4


-- Sale
INSERT INTO sale (customer_id, sale_date, ship_date)
VALUES
 (1, '2022-01-01', null),           -- id=1
 (1, '2022-02-01', '2022-02-02'),   -- id=2
 (2, '2022-03-01', null),           -- id=3
 (2, '2022-01-01', '2022-01-02');   -- id=4

-- Line_item
INSERT INTO line_item (	sale_id, product_id, quantity)
VALUES
 (1, 1, 1),
 (1, 2, 1),
 (1, 4, 1),
 (2, 4, 10),
 (2, 1, 10),
 (3, 1, 100);

COMMIT;
