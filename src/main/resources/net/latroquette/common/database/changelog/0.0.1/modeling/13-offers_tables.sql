-- Table: item_lists

CREATE TABLE IF NOT EXISTS item_lists
(
  item_list_id serial NOT NULL,
  user_id integer NOT NULL,
  item_list_creation_date time without time zone NOT NULL DEFAULT now(),
  CONSTRAINT item_lists_pkey1 PRIMARY KEY (item_list_id),
  CONSTRAINT item_lists_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

-- Table: item_list_lines

CREATE TABLE IF NOT EXISTS item_list_lines
(
 item_line_id serial NOT NULL,
  item_id integer,
  wish_id integer,
  item_list_id integer,
  CONSTRAINT item_lists_pkey PRIMARY KEY (item_line_id),
  CONSTRAINT item_lines_item_list_id_fkey FOREIGN KEY (item_list_id)
      REFERENCES item_lists (item_list_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_lists_item_id_fkey FOREIGN KEY (item_id)
      REFERENCES items (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_lists_wish_id_fkey FOREIGN KEY (wish_id)
      REFERENCES wishes (wish_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS offers
(
  offer_id serial NOT NULL,
  user_creator_id integer NOT NULL,
  user_recipent_id integer NOT NULL,
  item_list_1_id integer,
  item_list_2_id integer,
  offer_message text,
  offer_status smallint NOT NULL DEFAULT 0,
  offer_parent_id integer,
  offer_creation_date time without time zone NOT NULL DEFAULT now(),
  offer_update_date time with time zone NOT NULL DEFAULT now(),
  CONSTRAINT offers_pkey PRIMARY KEY (offer_id),
  CONSTRAINT offers_item_list_1_id_fkey FOREIGN KEY (item_list_1_id)
      REFERENCES item_lists (item_list_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT offers_item_list_2_id_fkey FOREIGN KEY (item_list_2_id)
      REFERENCES item_lists (item_list_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT offers_user_creator_id_fkey FOREIGN KEY (user_creator_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT offers_user_recipent_id_fkey FOREIGN KEY (user_recipent_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);