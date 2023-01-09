CREATE database bank;
use bank;

-- CONTA POUPANCA
create table savings_account(
	id serial primary key,
	client_name varchar(200),
	cpf char(11),
	birthDate char(10),
	balance double precision,
	type_account char(2)
);

-- CONTA CORRENTE
create table checking_account(
	id serial primary key,
	client_name varchar(200),
	cpf char(11),
	birthDate char(10),
	balance double precision,
	type_account char(2)
);


-- TABELA DE CŔÉDITO DA CONTA CORRENTE
create table credit_checking_account(
	id serial primary key,
	cpf_account varchar(11),
	debit_balance double precision
);

select * from credit_checking_account cca;

select * from savings_account sa;

select * from checking_account ca;
