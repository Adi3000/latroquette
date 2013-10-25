-- Table: navigation

CREATE TABLE navigation
(
  nav_id serial NOT NULL,
  nav_parent_id integer,
  nav_path character varying(50),
  nav_label character varying(100),
  CONSTRAINT navigation_pkey PRIMARY KEY (nav_id),
  CONSTRAINT navigation_nav_parent_id_fkey FOREIGN KEY (nav_parent_id)
      REFERENCES navigation (nav_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE navigation
  OWNER TO latroquette;
  
-- Sequence: navigation_nav_id_seq

CREATE SEQUENCE navigation_nav_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE navigation_nav_id_seq
  OWNER TO latroquette;