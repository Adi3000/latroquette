create or replace view user_statistics as 
select u.user_id, u.user_login, u.user_mail, u.user_login_state, max(i.item_update_date) as last_item_update, 
count(i.item_id) as nb_items, count(draft.item_id) as nb_drafts, smf_id_member, smf.is_activated as smf_is_activated
from users u
left join items i using(user_id)
left join items draft on u.user_id = draft.user_id and draft.item_status_id = 0
left join forum.smf_members smf on  smf.id_member = u.smf_id_member
group by  u.user_id, u.user_login, smf_id_member, smf.is_activated, u.user_login_state