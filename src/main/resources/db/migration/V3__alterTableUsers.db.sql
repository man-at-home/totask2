-- integrate userdetails spring security
-- ------------------------------------------
-- @author man-at-home

ALTER TABLE tt_user ADD IF NOT EXISTS is_admin  boolean default false not null;

ALTER TABLE tt_user ADD IF NOT EXISTS password  varchar(100);


update tt_user
   set is_admin = 1
 where user_name = 'admin';

commit;
