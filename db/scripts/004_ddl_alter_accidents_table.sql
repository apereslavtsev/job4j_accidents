ALTER TABLE public.accidents 
ADD COLUMN text TEXT,
ADD COLUMN adress TEXT;

UPDATE accidents SET text = 'text car theft in Moskow', 
adress = 'Moskow'  WHERE id = 1;

UPDATE accidents SET text = 'text driving on red in Saint-Petersburg', 
adress = 'Saint-Petersburg'  WHERE id = 2;

UPDATE accidents SET text = 'text drunk driving in Saratov', 
adress = 'Saratov'  WHERE id = 3;