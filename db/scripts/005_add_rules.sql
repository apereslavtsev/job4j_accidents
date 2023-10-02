CREATE TABLE rules (
   id SERIAL PRIMARY KEY,
   name TEXT NOT NULL
);

INSERT INTO rules (id, name) VALUES (1, 'Статья. 1');
INSERT INTO rules (id, name) VALUES (2, 'Статья. 2');
INSERT INTO rules (id, name) VALUES (3, 'Статья. 3');


CREATE TABLE accident_rules (
   id serial PRIMARY KEY,
   accident_id int not null REFERENCES accidents(id),
   rule_id int not null REFERENCES rules(id),
   UNIQUE (accident_id, rule_id)
);

INSERT INTO accident_rules (accident_id, rule_id) VALUES (1, 1);
INSERT INTO accident_rules (accident_id, rule_id) VALUES (1, 2);
INSERT INTO accident_rules (accident_id, rule_id) VALUES (2, 3);
INSERT INTO accident_rules (accident_id, rule_id) VALUES (2, 2);
INSERT INTO accident_rules (accident_id, rule_id) VALUES (3, 3);
