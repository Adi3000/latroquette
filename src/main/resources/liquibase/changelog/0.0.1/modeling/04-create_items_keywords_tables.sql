CREATE TABLE IF NOT EXISTS items_external_keywords
(
  item_id integer NOT NULL,
  ext_keyword_id integer NOT NULL,
  CONSTRAINT items_external_keywords_pkey PRIMARY KEY (item_id, ext_keyword_id),
  CONSTRAINT items_external_keywords_ext_keyword_id_fkey FOREIGN KEY (ext_keyword_id)
      REFERENCES external_keywords (ext_keyword_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT items_external_keywords_item_id_fkey FOREIGN KEY (item_id)
      REFERENCES items (item_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items_keywords
(
  item_id integer NOT NULL,
  keyword_id integer NOT NULL,
  CONSTRAINT items_keywords_pkey PRIMARY KEY (item_id, keyword_id),
  CONSTRAINT items_keywords_item_id_fkey FOREIGN KEY (item_id)
      REFERENCES items (item_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT items_keywords_keyword_id_fkey FOREIGN KEY (keyword_id)
      REFERENCES keywords (keyword_id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);