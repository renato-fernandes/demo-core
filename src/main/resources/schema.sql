-- Users
--DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users(
    id serial primary key,
    username varchar(100) not null,
    name varchar(100),
    lastName varchar(100) ,
    email varchar(255) not null,
    password varchar(255) not null,
    status smallint DEFAULT 1,
    createdAt timestamp not null,
    updatedAt timestamp
);