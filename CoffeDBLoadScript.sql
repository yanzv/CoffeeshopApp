			
CREATE TABLE coffees
(
	coffeeID INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	ProdNum CHAR(6) NOT NULL,
	Description VARCHAR(25) NOT NULL,
	Price DECIMAL(10,2) NOT NULL
);

CREATE TABLE customers
(
	customerID INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	Name VARCHAR(25) NOT NULL,
	Address VARCHAR(40),
	City VARCHAR(30),
	State CHAR(2),
	Zip VARCHAR(5),
	Balance DECIMAL(10,2)
);

 CREATE TABLE orders
 (
 	orderID INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
 	customerID INTEGER NOT NULL,
 	coffeeID INTEGER NOT NULL,
 	quantity INTEGER NOT NULL,
 	Cost DECIMAL(10,2) NOT NULL,
 	orderDate DATE NOT NULL
 );


alter table orders
   add constraint fk foreign key (customerID)
                     references Customers(customerID)
;


alter table Orders
   add constraint fk2 foreign key (coffeeID)
                     references Coffees(coffeeID)   
;

insert into Customers(name,balance)
   values ( 'David'   , 0.00 ),
          ( 'Meir'    , 0.00 ),
          ( 'Sam'     , 0.00 ),
          ( 'Yehuda'  , 0.00 ),
          ( 'Yitshak' , 0.00 ) 
;

insert into coffees(description, price, ProdNum)
				values('Mocha',1.25,'14-001'),
				('Italian',1.50,'14-002'),
				('Columbian', 1.50,'14-003'),
				('Breakfast Special',2.00,'14-004');
	


CREATE TRIGGER OrdersInsertTrigger
  AFTER INSERT ON Orders
  MODE DB2SQL 
update Customers
 set balance = 
(
	select sum(cost)
	from orders, coffees
	where orders.coffeeID = coffees.coffeeID AND orders.customerid = customers.customerid
	group by orders.customerid
);

/*
insert into Orders(customerID,coffeeID,quantity)
   values (1 , 2 , 2 ) ,
		(2 , 4 , 4 ) ,
		(3 , 2 , 3 ) ,
		(3 , 1 , 5 ) ,
		(4 , 3 , 1 ) 
;
*/