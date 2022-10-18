CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   name VARCHAR NOT NULL UNIQUE,
   description TEXT,
   created TIMESTAMP NOT NULL,
   done BOOLEAN
);