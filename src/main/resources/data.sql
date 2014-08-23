
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(1, 'admin', 'predefined admin user', false, 1);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION) values(2, 'unit-test-user', 'unit-test user data.sql', true, 1);

insert into tt_project (id, name) values (1, 'totask2');
insert into tt_project (id, name) values (2, 'demo-project');

insert into tt_task(id, name, project_id) values(1, 'develop',  1);
insert into tt_task(id, name, project_id) values(2, 'support',  1);
insert into tt_task(id, name, project_id) values(3, 'document', 1);

insert into tt_task(id, name, project_id) values(4, 'develop', 2);  
insert into tt_task(id, name, project_id) values(5, 'test',    2);

insert into tt_workentry(id, comment, task_id, user_id, duration, at) values(1, 'entry 1', 4, 2, 2, TODAY);
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values(2, 'entry 2', 4, 2, 3, TODAY);
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values(3, 'entry 3', 5, 2, 4, TODAY);
