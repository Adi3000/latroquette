insert into parameters (param_id,param_name, param_value, param_data_type, param_description)
values
(7,'TOKEN_DELAY','2 days', 's1', 'Postgres interval Delay before a token expire'),
(8,'XMPP_SECURE','N', 'b1', 'Using https for XMPP HTTP binding'),
(9,'XMPP_NODE','latroquette.net', 's1', 'XMPP node name'),
(10,'XMPP_HOST','latroquette.net', 's1', 'XMPP host name'),
(11,'XMPP_URI_PATH','/http-bind', 's1', 'XMPP HTTP bind URI'),
(12,'XMPP_PORT','5280', 'n1', 'XMPP port HTTP bind URI');
