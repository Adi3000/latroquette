CREATE TABLE IF NOT EXISTS wishes
(
   wish_id serial, 
   wish_name character varying(255), 
   keyword_id integer, 
   source_id character(3), 
   ext_keyword_id integer, 
   ext_uid character varying, 
    PRIMARY KEY (wish_id), 
    FOREIGN KEY (keyword_id) REFERENCES keywords (keyword_id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
    FOREIGN KEY (ext_keyword_id) REFERENCES external_keywords (ext_keyword_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS users_wishes
(
   user_id integer, 
   wish_id integer, 
    PRIMARY KEY (user_id, wish_id), 
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
    FOREIGN KEY (wish_id) REFERENCES wishes (wish_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);