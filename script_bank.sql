CREATE database bank;
use bank;

create table owner(
	cpf varchar(11) primary key,
	name varchar(200),
	birthDate char(10)
);

-- CONTA POUPANCA
create table savings_account(
	id serial primary key,
	owner_id varchar(11),
	balance double precision,
	type_account char(2),
	CONSTRAINT fk_owner FOREIGN KEY(owner_id) REFERENCES owner(cpf)
);

-- CONTA CORRENTE
create table checking_account(
	id serial primary key,
	owner_id varchar(11),
	balance double precision,
	type_account char(2),
	CONSTRAINT fk_owner FOREIGN KEY(owner_id) REFERENCES owner(cpf)
);


-- TABELA DE CRÉDITO DA CONTA CORRENTE
create table credit_checking_account(
	id serial primary key,
	cpf_account varchar(11),
	debit_balance double precision
);

-- TABELA DE HISTORICO
create table history(
    cpf_account varchar(11),
    type_account char(2),
    created_at timestamp default NOW(),
    movement varchar(200)
);

select * from credit_checking_account cca;

select * from savings_account sa;

select * from checking_account ca;
