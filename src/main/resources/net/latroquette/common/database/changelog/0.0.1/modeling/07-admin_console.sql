CREATE OR REPLACE VIEW user_statistics AS 
 SELECT u.user_id, u.user_login, u.user_mail, u.user_login_state, 
    max(i.item_update_date) AS last_item_update, count(i.item_id) AS nb_items, 
    count(draft.item_id) AS nb_drafts, u.smf_id_member, 
    smf.is_activated AS smf_is_activated,  u.role_id
   FROM users u
   LEFT JOIN items i USING (user_id)
   LEFT JOIN items draft ON u.user_id = draft.user_id AND draft.item_status_id = 0 AND draft.item_id = i.item_id
   LEFT JOIN forum.smf_members smf ON smf.id_member = u.smf_id_member
  GROUP BY u.user_id, u.user_login, u.smf_id_member, smf.is_activated,  u.role_id, u.user_login_state;

CREATE TABLE roles
(
  role_id serial NOT NULL,
  role_label character varying(20) NOT NULL,
  modify_keywords character(1) NOT NULL DEFAULT 'N'::bpchar,
  validate_items character(1) NOT NULL DEFAULT 'N'::bpchar,
  is_admin character(1) NOT NULL DEFAULT 'N'::bpchar,
  CONSTRAINT roles_pkey PRIMARY KEY (role_id)
);
  
alter table users
	add column is_xmpp character(1) DEFAULT 'N'::bpchar NOT NULL;
	
ALTER TABLE users
  ADD COLUMN role_id integer;
ALTER TABLE users
  ADD FOREIGN KEY (role_id) REFERENCES roles (role_id) ON UPDATE NO ACTION ON DELETE NO ACTION;	