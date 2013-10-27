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
