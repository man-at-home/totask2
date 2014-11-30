-- test user creation

insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(1, 'admin', 'predefined admin user', true, 1);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(2, 'unit-test-user', 'unit-test user data.sql', true, 1);

-- totask project 1
insert into tt_project (id, name) values (1, 'totask2');

insert into tt_task(id, name, project_id) values(1, 'develop',  1);
insert into tt_task(id, name, project_id) values(2, 'support',  1);
insert into tt_task(id, name, project_id) values(3, 'document', 1);

commit;
