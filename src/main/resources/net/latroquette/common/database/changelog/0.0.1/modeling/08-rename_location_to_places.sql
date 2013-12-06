CREATE TABLE place_types
(
  place_type_id integer NOT NULL,
  place_type_label character varying(10) NOT NULL,
  CONSTRAINT place_types_pkey PRIMARY KEY (place_type_id)
);

INSERT INTO place_types (place_type_id,place_type_label )
select location_type_id, location_type_label from location_type;


CREATE TABLE places
(
  place_id serial NOT NULL,
  place_name character varying(180),
  place_longitude numeric,
  place_latitude numeric,
  place_postal_codes character varying(180), -- List of postal codes
  place_type_id integer,
  place_id_l1 integer,
  place_id_l2 integer,
  place_id_l3 integer,
  place_id_l4 integer,
  CONSTRAINT place_pkey PRIMARY KEY (place_id)
);

INSERT INTO places (place_id,
  place_name,
  place_longitude ,
  place_latitude ,
  place_postal_codes ,
  place_type_id ,
  place_id_l1 ,
  place_id_l2 ,
  place_id_l3 ,
  place_id_l4 )
  SELECT
	location_id serial,
	location_name ,
	location_longitude,
	location_latitude ,
	location_postal_codes,
	location_type_id, 
	location_id_l1 ,
	location_id_l2 ,
	location_id_l3 ,
	location_id_l4 
from  locations;

ALTER TABLE places
	add CONSTRAINT place_place_id_l1_fkey FOREIGN KEY (place_id_l1)
	      REFERENCES places (place_id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
	add CONSTRAINT place_place_id_l2_fkey FOREIGN KEY (place_id_l2)
	      REFERENCES places (place_id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
	add CONSTRAINT place_place_id_l3_fkey FOREIGN KEY (place_id_l3)
	      REFERENCES places (place_id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
	add CONSTRAINT place_place_id_l4_fkey FOREIGN KEY (place_id_l4)
	      REFERENCES places (place_id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE,
	add CONSTRAINT place_place_type_id_fkey FOREIGN KEY (place_type_id)
	      REFERENCES place_types (place_type_id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION;

drop table locations;
drop table location_type;

ALTER TABLE users
  ADD COLUMN place_id integer;
ALTER TABLE users
  ADD FOREIGN KEY (place_id) REFERENCES places (place_id) ON UPDATE RESTRICT ON DELETE RESTRICT;
