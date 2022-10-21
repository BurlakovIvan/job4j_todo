CREATE TABLE if not exists users (
  id SERIAL PRIMARY KEY,
  name varchar NOT NULL,
  login varchar,
  password varchar,
  UNIQUE (login)
);