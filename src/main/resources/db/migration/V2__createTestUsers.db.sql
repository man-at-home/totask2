-- test user creation

insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(1, 'admin', 'predefined admin user', false, 1);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(2, 'unit-test-user', 'unit-test user data.sql', true, 1);

commit;
