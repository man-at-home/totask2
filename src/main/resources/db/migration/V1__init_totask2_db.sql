-- initial totask2 db flyway migration script
-- ------------------------------------------
-- @author man-at-home

-- tables
-- ------

create table tt_project (
	id bigint generated by default as identity, 
	name varchar(250) not null, 
	primary key (id)
);

create table tt_task (
	id bigint generated by default as identity, 
	name varchar(250) not null, 
	project_id bigint not null, 
	primary key (id)
);

create table tt_user (
	id bigint generated by default as identity, 
	active boolean not null, 
	display_name varchar(250) not null, 
	user_name varchar(50) not null, 
	version bigint not null, 
	primary key (id)
);

create table tt_workentry (
	id bigint generated by default as identity, 
	at date not null, 
	comment varchar(250), 
	duration float not null check (duration>=0 AND duration<=24), 
	task_id bigint not null, 
	user_id bigint not null, 
	primary key (id)
);


-- constraints
-- -----------
alter table tt_user      add constraint IDX_TT_USER_NAME         unique (user_name);

-- foreign keys
-- ------------
alter table tt_task      add constraint FK_TT_TASK_OWNING_PARENT foreign key (project_id) references tt_project;
alter table tt_workentry add constraint FK_TT_WORKENTRY_TASK     foreign key (task_id)    references tt_task;
alter table tt_workentry add constraint FK_TT_WORKENTRY_USER     foreign key (user_id)    references tt_user;
