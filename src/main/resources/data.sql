
insert into tt_user(USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values('admin', 'predefined admin user', false, 1);
insert into tt_user(USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values('init-data', 'init-data', true, 1);

insert into tt_project (id, name) values (1, 'totask2');
insert into tt_project (id, name) values (2, 'demo-project');

insert into tt_task(id, name, project_id) values(1, 'develop', 1);
insert into tt_task(id, name, project_id) values(2, 'support', 1);
insert into tt_task(id, name, project_id) values(3, 'document', 1);