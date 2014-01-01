-- Table: item_lists

CREATE TABLE IF NOT EXISTS item_lists
(
  item_list_id integer serial NOT NULL,
  user_id integer NOT NULL,
  user_creation_date time without time zone NOT NULL DEFAULT now(),
  CONSTRAINT item_lists_pkey1 PRIMARY KEY (item_list_id),
  CONSTRAINT item_lists_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: item_list_lines

CREATE TABLE IF NOT EXISTS item_list_lines
(
  item_list_line_id serial NOT NULL,
  item_list_id integer NOT NULL,
  item_id integer,
  wish_id integer,
  CONSTRAINT item_list_lines_pkey PRIMARY KEY (item_list_line_id),
  CONSTRAINT item_lines_item_lists_item_id_fkey FOREIGN KEY (item_id)
      REFERENCES items (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_lines_item_lists_wish_id_fkey FOREIGN KEY (wish_id)
      REFERENCES wishes (wish_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
