-- test data

insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION, IS_ADMIN, PASSWORD) values(1, 'admin', 'predefined admin user', true, 1, true, null);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION, IS_ADMIN, PASSWORD) values(2, 'unit-test-user', 'unit-test user data.sql', true, 1, false, null);

-- totask project 1
insert into tt_project (id, name) values (1, 'totask2');

insert into tt_task(id, name, project_id) values(1, 'develop',  1);
insert into tt_task(id, name, project_id) values(2, 'support',  1);
insert into tt_task(id, name, project_id) values(3, 'document', 1);


-- demo project 2
insert into tt_project (id, name) values (2, 'demo-project');

insert into tt_task(id, name, project_id) values(4, 'demo-develop', 2);  
insert into tt_task(id, name, project_id) values(5, 'demo-test',    2);
insert into tt_task(id, name, project_id) values(6, 'demo-doc',     2);

insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 1, 'entry 4.0 dev' , 4, 2,   4, TODAY);
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 2, 'entry 4.1 dev' , 4, 2, 4.1, DATEADD('DAY', 1, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 3, 'entry 4.2 dev' , 4, 2, 4.2, DATEADD('DAY', 2, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 4, 'entry 4.4 dev' , 4, 2, 4.4, DATEADD('DAY', 4, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 5, 'entry 4.1- dev', 4, 2, 4.1, DATEADD('DAY',-1, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values(10, 'entry 5.0 test', 5, 2,   5, TODAY);

commit;
