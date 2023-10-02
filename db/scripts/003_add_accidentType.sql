CREATE TABLE [IF NOT EXISTS] accident_types (
   id SERIAL PRIMARY KEY,
   name TEXT NOT NULL
);

INSERT INTO accident_types (id, name) VALUES (1, 'Две машины');
INSERT INTO accident_types (id, name) VALUES (2, 'Машина и человек');
INSERT INTO accident_types (id, name) VALUES (3, 'Машина и велосипед');

ALTER TABLE accidents ADD COLUMN accident_type_id int REFERENCES accident_types(id);

UPDATE accidents SET accident_type_id = 1 WHERE id = 1;
UPDATE accidents SET accident_type_id = 2 WHERE id = 2;
UPDATE accidents SET accident_type_id = 3 WHERE id = 3;