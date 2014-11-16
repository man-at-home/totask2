-- test data

insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION, IS_ADMIN, PASSWORD) values(1, 'admin',           'predefined admin user', true, 1, true , null);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION, IS_ADMIN, PASSWORD) values(2, 'unit-test-user',  'unit-test user'       , true, 1, false, null);
insert into tt_user(ID, USER_NAME, DISPLAY_NAME, ACTIVE, VERSION, IS_ADMIN, PASSWORD) values(3, 'unit-test-admin', 'unit-test admin'      , true, 1, true , null);



-- totask project 1
insert into tt_project (id, name) values (1, 'totask2');

insert into tt_task(id, name, project_id) values(1, 'develop',  1);
insert into tt_task(id, name, project_id) values(2, 'support',  1);
insert into tt_task(id, name, project_id) values(3, 'document', 1);


-- demo project 2
insert into tt_project (id, name) values (2, 'demo-project');

insert into tt_task(id, name, project_id) values(4, 'demo-develop',        2);  
insert into tt_task(id, name, project_id) values(5, 'demo-test',           2);
insert into tt_task(id, name, project_id) values(6, 'demo-doc 14days',     2);

insert into tt_task_assignment (id, starting_from, task_id, user_id)        values (  1, DATEADD('DAY',-20, TODAY), 4,  2);
insert into tt_task_assignment (id, starting_from, task_id, user_id)        values (  2, DATEADD('DAY',-10, TODAY), 5,  2);
insert into tt_task_assignment (id, starting_from, task_id, user_id, until) values (  3, DATEADD('DAY',-50, TODAY), 6,  2, DATEADD('DAY', 14, TODAY));

insert into tt_task_assignment (id, starting_from, task_id, user_id, until) values (  4, DATEADD('DAY',-20, TODAY), 4,  1, DATEADD('DAY',-10, TODAY));
insert into tt_task_assignment (id, starting_from, task_id, user_id)        values (  5, DATEADD('DAY',-50, TODAY), 6,  1);


insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 1, 'entry 4.0 dev' , 4, 2,   4, TODAY);
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 2, 'entry 4.1 dev' , 4, 2, 4.1, DATEADD('DAY', 1, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 3, 'entry 4.2 dev' , 4, 2, 4.2, DATEADD('DAY', 2, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 4, 'entry 4.4 dev' , 4, 2, 4.4, DATEADD('DAY', 4, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values( 5, 'entry 4.1- dev', 4, 2, 4.1, DATEADD('DAY',-1, TODAY));
insert into tt_workentry(id, comment, task_id, user_id, duration, at) values(10, 'entry 5.0 test', 5, 2,   5, TODAY);

commit;
