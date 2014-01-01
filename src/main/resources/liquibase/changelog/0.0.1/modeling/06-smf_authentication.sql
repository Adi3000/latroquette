alter table users
	add column user_login_state smallint not null default 0,
	add column smf_id_member integer