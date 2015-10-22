drop table if exists 
plans,
plans_to_modules,
modules,
-- disciplines_to_module_types,
modules_to_disciplines_to_module_types,
module_nrs,
modules_to_disciplines, 
departments_to_modules, 
departments, 
employees,   
courses, 
lecturers_to_courses, 
courses_times_and_rooms,
employees_to_roles,
last_modified_log,
course_module_parts,
day_abbr
cascade;
--  
 --Drop only if needed
-- drop type if exists  
-- day_abbr cascade; 
-- -- time_interval
-- cascade; 

--Drop only if needed
drop table if exists
disciplines,
module_types,
assessment_types,
roles, 
course_types cascade;
 

-- types
--========================================
-- create type time_interval as(
-- 	start_time time,
-- 	end_time time
-- );
-- 
create table day_abbr (
	day text primary key
); 
-- 

--End types
--=========================================


--Tables
--=========================================
--Preexisting tables
--=========================================

create table assessment_types (-- Problem: there are abbreviations (MP) and words associated with it: (Modulprüfung in der VL freien Zeit) --> not simply an enum..
	abbr text primary key,
	description text
);
-- Leistungsnachweise 
-- assessment_types needs to be filled with: 

 create table course_types( -- e.g. 'Blockkurs' 
	abbr text primary key,
	description text
);

-- course_types has to be filled with:
create table module_types(
	abbr text primary key,
	description text 
);

-- module_types needs to be filled with: 
--Disciplines needs to be filled with: MSc, BSc, SpezMSc, Graduate School (GS?), PHZH, NF
create table disciplines ( -- e.g. MSc, BSc, etc.
	abbr text primary key,
	description text
);	


create table roles (  
	abbr text primary key,
	description text
);	

select * from roles;
--End preexisting tables
--=================================================
--Simple tables 
create table plans( 
-- 	version_nr text, -- used to copy a plan, not simply overwriting when changes occur. should be a unix timestamp or sth
	id serial primary key,
	semester text,
	year integer
);
----Problem with inheritance was too severe, switched to old style of having nullable values instead...
create table employees(
	id serial primary key,
	employee_nr text, -- 'Mitarbeiternummer' --> cannot be pk because of people from outside that don't have one... so took the email address... 
	first_name text,
	last_name text,
	email text,
	internal_cost_center integer, -- Kostenstelle, internal = internally employed. External = externally hired
	external_institute text, -- for those paid via contracts 
	is_external_paid_separately boolean not null, -- x that goes into the other column, for people that are e.g. from another company
	username text unique, -- username for login
	password text, -- password for login
	comments text 
); 
create table modules(
	id  serial primary key , 
	semester_nr text, -- yes, a string.... cause some people wrote an 'S' there...
 	assessment_type_fk text references assessment_types(abbr) on delete cascade on update cascade,
 	assessment_date text,
 	responsible_employee int references employees(id) on delete cascade on update cascade,
	comments text
);
-- lehrveranstaltung lva == course 
 create table courses( 
	id  serial unique ,
	course_description text, -- part of pk now...
 	module_id_fk int references modules(id) on delete cascade on update cascade, -- primary key of the owner entity 
	vvznr text, -- possibly part of the pk, maybe 'new', thus I put it as string... need to check on that I guess
	nr_of_groups int,
	nr_of_students_expected_per_group int,
	is_max_nr_students_expected_per_group bool, -- flag for is this the max number of students expected for that group? --> will have a 'max' indication
	sws_tot_per_group real,
	date text,   
	start_date date, -- e.g. '1. Woche'
	end_date date,
	rythm text, -- e.g. ca. 3 Termine 
	comments text,
	course_type_fk text references course_types(abbr) on delete cascade on update cascade,
 	primary key (id, module_id_fk)
);
  create table courses_times_and_rooms( 
	id serial unique,
	course_id_fk int ,
	module_id_fk int , -- primary key of the owner entity  
	foreign key (course_id_fk, module_id_fk) references courses(id, module_id_fk) on delete cascade on update cascade,
	start_time time,
	end_time time,
	day_of_week text,
	room_capacity int,
	room_label text,
	comments text,
	primary key (id, course_id_fk, module_id_fk)
); 


  
create table departments ( -- Abteilung
	id serial primary key,
	dept_name text,
	field_of_expertise text -- Fachgebiet 
);
 
-- End simple tables
-- ========================================================
-- M to N tables and other complex tables  
create table module_nrs(
	module_id_fk int references modules(id) on delete cascade on update cascade,
	module_nr text,
	primary key (module_id_fk, module_nr) 
);

 
create table employees_to_roles(
	employee_fk int references employees(id) on delete cascade on update cascade,
	role_fk text references roles(abbr) on delete cascade on update cascade,
	primary key (employee_fk, role_fk)
);

create table plans_to_modules ( 
-- 	plan_version_nr_fk text, -- used to copy a plan, not simply overwriting when changes occur
	plan_id_fk int,
	module_id_fk int,
	primary key (plan_id_fk, module_id_fk),
	foreign key (plan_id_fk) references plans(id) on delete cascade on update cascade,
	foreign key (module_id_fk) references modules(id) on delete cascade on update cascade
);
  

 create table course_module_parts (
	course_id_fk int references courses(id) on delete cascade on update cascade,
	module_id_fk int references modules(id) on delete cascade on update cascade,
	module_part text,
	primary key (course_id_fk, module_id_fk, module_part)
);
-- create table modules_to_disciplines (
-- 	
-- 	discipline_fk text references disciplines(abbr) on delete cascade on update cascade,
-- 	primary key (module_id_fk, discipline_fk)
-- );
create table modules_to_disciplines_to_module_types(
	module_id_fk int references modules(id) on delete cascade on update cascade,
	discipline_fk text references disciplines(abbr) on delete cascade on update cascade,
 	module_type_fk text references module_types(abbr) on delete cascade on update cascade,
	primary key (module_id_fk, discipline_fk, module_type_fk)
); 
create table departments_to_modules(
	module_id_fk int references modules(id) on delete cascade on update cascade,
	dept_id_fk int references departments(id) on delete cascade on update cascade,
	primary key (module_id_fk, dept_id_fk)
); 

 
create table lecturers_to_courses( 
	course_id_fk int ,
	module_id_fk int , -- primary key of the owner entity  
	lecturer_fk int references employees(id) on delete cascade on update cascade,
	foreign key (course_id_fk, module_id_fk) references courses(id, module_id_fk) on delete cascade on update cascade,
	primary key(course_id_fk, module_id_fk, lecturer_fk)
);



 -- End M to N tables and other complex tables 
-- ========================================================
 ----Last modified table
create table last_modified_log(
	id serial primary key,
	table_name text not null,
	id_in_table text not null, --as it may be an abbreviation (text) and not only an int
	modifying_employee text not null,
	modifying_date timestamp not null
);
-- Tables end 
-- delete from modules_to_disciplines_to_module_types where module_id_fk=1 discipline_fk='MSc' and module_type_fk='PF';
-- select * from modules_to_disciplines_to_module_types;
-- update courses set is_max_nr_students_expected_per_group=true where module_id_fk = 9 AND id = 29;