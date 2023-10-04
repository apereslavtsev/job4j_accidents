ALTER TABLE accidents ADD COLUMN IF NOT EXISTS text TEXT;
ALTER TABLE accidents ADD COLUMN IF NOT EXISTS address TEXT;

UPDATE accidents SET text = 'text car theft in Moskow', 
address = 'Moskow'  WHERE id = 1;

UPDATE accidents SET text = 'text driving on red in Saint-Petersburg', 
address = 'Saint-Petersburg'  WHERE id = 2;

UPDATE accidents SET text = 'text drunk driving in Saratov', 
address = 'Saratov'  WHERE id = 3;