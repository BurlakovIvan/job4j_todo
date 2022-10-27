CREATE TABLE if not exists priorities (
   id SERIAL PRIMARY KEY,
   name TEXT NOT NULL,
   position int
);