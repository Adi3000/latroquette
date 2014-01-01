ALTER TABLE users DROP COLUMN user_token;
ALTER TABLE users DROP COLUMN user_last_host_name_login;
ALTER TABLE users ADD COLUMN user_token character varying(32);
ALTER TABLE users ADD COLUMN user_salt character varying(40);
ALTER TABLE users ADD COLUMN user_last_ip_xmpp character varying(40);
ALTER TABLE users ADD COLUMN user_last_date_xmpp timestamp without time zone;
ALTER TABLE users ADD COLUMN user_verification_token character varying(32);
ALTER TABLE users ADD COLUMN user_date_verification_token timestamp without time zone;
