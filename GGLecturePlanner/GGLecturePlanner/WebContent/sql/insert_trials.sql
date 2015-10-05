 --add preexisting types
insert into assessment_types values ('MP', 'Modulprüfung (in der Vorlesungsfreien Zeit)');
insert into assessment_types values ('MT', 'Mid-term- / Zwischenprüfung');
insert into assessment_types values ('ET', 'End-term- / Schlussprüfung');
insert into assessment_types values ('UE', 'Lösen von Übungsaufgaben (als Leistungsnachweis)');
insert into assessment_types values ('SA', 'schriftliche Arbeit');
insert into assessment_types values ('SV', 'Seminarvortrag');
insert into assessment_types values ('TU', 'Tutorielle Tätigkeit (als Leistungsnachweis)');
insert into assessment_types values ('PP', 'Poster/Präsentation');

insert into day_abbr values('Mo');
insert into day_abbr values('Di');
insert into day_abbr values('Mi');
insert into day_abbr values('Do');
insert into day_abbr values('Fr');
insert into day_abbr values('Sa');
insert into day_abbr values('So');

insert into course_types values ('VL', 'Vorlesung');
insert into course_types values ('UE', 'Übungen');
insert into course_types values ('VU', 'Vorlesung mit integrierter Übung');
insert into course_types values ('PR', 'Praktikum');
insert into course_types values ('BL', 'Blockkurs');
insert into course_types values ('TU', 'Tutorat');
insert into course_types values ('PS', 'Proseminar');
insert into course_types values ('SE', 'Seminar');
insert into course_types values ('E', 'Exkursion / Feldkurs');
insert into course_types values ('KO', 'Kolloquium');

insert into module_types values ('PF', 'Pflichtmodul');
insert into module_types values ('WP', 'Wahlpflichtmodul');
insert into module_types values ('WM', 'Wahlmodul'); 

insert into disciplines values ('BSc', 'Bachelorprogramm 120/150/180 ECTS');
insert into disciplines values ('MSc', 'Masterprogramm 90 ECTS');
insert into disciplines values ('Spez. MSc Hum', 'Master of Science UZH in Geography – Geographies of Global Change: Resources, Markets and Development');
insert into disciplines values ('Spez. MSc GIScience', 'Master of Science UZH in Geography – Geographic Information Science (GIScience)');
insert into disciplines values ('Spez. MSc RSL', 'Master of Science UZH in Geography – Remote Sensing');
insert into disciplines values ('Spez. MSc Phys', 'Master of Science UZH in Geography – Physical Geography');
insert into disciplines values ('NF30', 'Nebenfach 30');
insert into disciplines values ('NF60', 'Nebenfach 60');
insert into disciplines values ('LD', 'Lehrdiplomausbildung');
insert into disciplines values ('PHZH', 'Pädagog. Hochschule ZH'); 

----Not yet done
insert into roles values ('Admin', 'Administrator');
insert into roles values ('MV', 'Modulverantwortlicher');
insert into roles values ('Doz ohne PhD', 'Lehrbeauftragte/r ohne PhD');
insert into roles values ('Doz mit PhD', 'Lehrbeauftragte/r mit PhD');
insert into roles values ('Doz ex annex', 'Lehrbeauftragte/r extern (angestellt ETH, WSL, …) - keine Bezahlung'); 
insert into roles values ('Doz ex privat', 'Lehrbeauftragte/r extern (angestellt privat, kantonal, …) - Bezahlung');
insert into roles values ('PD/TP', 'Privatdozierende/Titularprofessor');
insert into roles values ('Prof', 'Professor (ausserord./ordentlich/SNF)');
 
 
 --add departments
insert into departments values (1,'3G','Physical Geography Glaciology and Geomorphodynamics');
insert into departments values (2,'2B','Physical Geography Soil Science and Biogeochemistry');
insert into departments values (3,'2K','Physical Geography Hydrology and Climate');
insert into departments values (4,'Geochrono','Physical Geography Geochronology');
insert into departments values (5,'Human','Human Geography');
insert into departments values (6,'Polit','Political Geography');
insert into departments values (7,'WGG','Economic Geography');
insert into departments values (8,'S&O','Space & Organisation');
insert into departments values (9,'RSL','Remote Sensing Laboratories');
insert into departments values (10,'MRS','Multimodal Remote Sensing');
insert into departments values (11,'GIS','GIScience: Geographic Information Systems');
insert into departments values (12,'GIVA','GIScience: Geographic Information Visualization');
insert into departments values (13,'Geocomp','GIScience: Geocomputation');
insert into departments values (14,'GTT','Geography Teacher Training');
insert into departments values (15,'HT','Head Teaching');

 ---- Add some lecturers 
insert into employees values (1, '1','Christian', 'Berndt', 'cb@geo.uzh.ch', null, null,false,'cberndt',null,null);
insert into employees values (2,'2','Christian', 'Huggel', 'ch@geo.uzh.ch', null, null,false,'chuggel',null,null);
insert into employees values (3,'3','Ross', 'Purves', 'rsp@geo.uzh.ch', null, null,false,'rpurves',null,null);
insert into employees values (4,'4','Andreas', 'Vieli', 'av@geo.uzh.ch', 70611,null,false,'avieli',null,null);
insert into employees values (5,'5','Jan', 'Seibert', 'js@geo.uzh.ch', 70611,null,false,'jseibert',null,null);
insert into employees values (6,'6','Michael', 'Schmidt', 'ms@geo.uzh.ch', 70610,null,false,'mschmidt',null,null);
insert into employees values (7,'7','Markus', 'Egli', 'me@geo.uzh.ch', 70610,null,false,'megli',null,null); 
insert into employees values (8,'A1','Yvonne', 'Scheidegger', 'ys@geo.uzh.ch', null,null,false,'yscheidegger','1234', null); 
insert into employees values (9,'A1','Oliver', 'Zihler', 'ozihler@geo.uzh.ch', null,null,false,'ozihler','1000:5b42403333393039373532:b46bf94225080ca578c99e35f318ebd879858a2e3ae1935525a3ebe4ad56322f369b8fcf1407c9a1a97f554f2c9dfeb409927ffe32c93d5bf590109236357940', null); 

select * from employees;
insert into employees_to_roles values (1, 'Prof');
insert into employees_to_roles values (2, 'Doz mit PhD');
insert into employees_to_roles values (3, 'Prof');
insert into employees_to_roles values (4, 'Prof');
insert into employees_to_roles values (5, 'Prof');
insert into employees_to_roles values (6, 'Prof');
insert into employees_to_roles values (7, 'Prof');
insert into employees_to_roles values (8, 'Admin');
insert into employees_to_roles values (9, 'Admin');
select * from lecturers_to_courses;
select * from employees;
Update employees set password = 'password1' where id=? and username=?;
 --====PLANS 
 insert into plans values (1, 'FS', 2015);
insert into plans values (2,'HS', 2015);
-- select * from plans;
-- select id as moduleid from modules where responsible_employee=9
-- union
-- select module_id_fk as moduleid from lecturers_to_courses where lecturer_fk=9;
--===MODULES  
insert into modules values (1,  1, 'PP', '', 1, 'keine (Posterfair)');
insert into modules values (2,  1, 'MP', 'KW2', 2,'');

---===Depts to modules
select * from departments;
insert into departments_to_modules values (1, 1);  
insert into departments_to_modules values (2, 2); 

----===PLANS_TO_MODULES
insert into plans_to_modules values (1, 1);
insert into plans_to_modules values (1, 2);
----===MODULES_TO_DISCIPLINES-- 
insert into modules_to_disciplines values (1, 'BSc');
insert into modules_to_disciplines values (2, 'BSc');

----===MODULES_TO_MODULE_TYPES 
insert into disciplines_to_module_types values(1, 'PF');
insert into disciplines_to_module_types values(2, 'PF');

insert into module_nrs values (1, 'PHZH 110');
insert into module_nrs values (2, 'GEO 111');
---- --===COURSES for that module
insert into courses values (1, 'Schweiz 2040', 1, 'NEU', null, 120, false, 2.0, '1. Woche', '','', '','VL');
insert into courses values (2, 'Übungen zu Schweiz 2040, Gruppe 1', 1, 'NEU', 4, 40, false, 1.0,  '1. Woche', '','ca. 3 Termine', '','UE'); 
insert into courses values (3, 'Übungen zu Schweiz 2040, Gruppe 2', 1, 'NEU', 4, 40, false, 1.0,  '1. Woche', '','ca. 3 Termine', '','UE'); 
insert into courses values (4, 'Übungen zu Schweiz 2040, Gruppe 3',1, 'NEU', 4, 40, false, 1.0,  '1. Woche', '','ca. 3 Termine', '','UE'); 

insert into courses values (5, 'Physische Geographie I: Grundzüge und Sphären', 2, '530',  null, 280, false, 2.0, '8. Woche', '','2. Semesterhälfte', '','VL');
insert into courses values (6, 'Besprechung zu Übung der Physischen Geographie I', 2, '531',  null, 160, false, 0.5, '8. Woche', '','2. Semesterhälfte', '','VL');
insert into courses values (7, 'Übungen der Physischen Geographie I, Gruppe 1', 2, '532',  null, null, false, 1.0,  '8. Woche', '','2. Semesterhälfte', '','UE');
insert into courses values (8, 'Übungen der Physischen Geographie I, Gruppe 2', 2, '533',  null, null, false, 1.0,  '8. Woche', '','2. Semesterhälfte', '','UE');
insert into courses values (9, 'Übungen der Physischen Geographie I, Gruppe 3', 2, '534',  null, null, false, 1.0,  '8. Woche', '','2. Semesterhälfte', '','UE');
insert into courses values (10, 'Übungen der Physischen Geographie I, Gruppe 4', 2, '535',  null, null, false, 1.0,  '8. Woche', '','2. Semesterhälfte', '','UE');  
 

insert into course_module_parts values (1, 1, 'Geo 111.1');
insert into course_module_parts values (2, 1, 'Geo 111.3');
insert into course_module_parts values (3, 2, 'Geo 111.2');
insert into course_module_parts values (4, 2, 'Geo 111.2');
insert into course_module_parts values (5, 2, 'Geo 111.2');
insert into course_module_parts values (6, 2, 'Geo 111.2');

---- --=== rooms and times for that course
insert into courses_times_and_rooms values (1, 1, 1, '13:00:00','14:45:00', 'Fr', null, 'Y15-G-60',null);
insert into courses_times_and_rooms values (2, 2, 1, '10:15:00','12:00:00', 'Mo', null, 'Y15-G-60',null);
insert into courses_times_and_rooms values (3, 3, 1, '12:15:00','13:00:00', 'Mo', 283, 'Y15-G-60',null);
insert into courses_times_and_rooms values (4, 4, 1, '14:00:00','15:45:00', 'Mo', 18, 'Y25-H-86','hello world');
insert into courses_times_and_rooms values (5, 5, 2, '16:15:00','18:00:00', 'Mo', 18, 'Y25-H-86',null);
insert into courses_times_and_rooms values (6, 6, 2, '15:00:00','17:00:00', 'Di', 18, 'Y25-H-86',null);
insert into courses_times_and_rooms values (7, 7, 2, '17:15:00','19:00:00', 'Di', 18, 'Y25-H-86',null);

-- --- Add some lecturers to some courses 
insert into lecturers_to_courses values (2, 1, 1);
insert into lecturers_to_courses values (2, 1, 2);
insert into lecturers_to_courses values (2, 1, 3);
insert into lecturers_to_courses values (2, 1, 4);
insert into lecturers_to_courses values (3, 1, 5);
insert into lecturers_to_courses values (3, 1, 2);
insert into lecturers_to_courses values (3, 1, 3);
insert into lecturers_to_courses values (3, 1, 4);
insert into lecturers_to_courses values (4, 1, 5);
insert into lecturers_to_courses values (4, 1, 2);
insert into lecturers_to_courses values (4, 1, 3);
insert into lecturers_to_courses values (4, 1, 4);
insert into lecturers_to_courses values (5, 2, 5);
insert into lecturers_to_courses values (5, 2, 2);
insert into lecturers_to_courses values (5, 2, 3);
insert into lecturers_to_courses values (5, 2, 4);
insert into lecturers_to_courses values (6, 2, 4);
insert into lecturers_to_courses values (6, 2, 5);
insert into lecturers_to_courses values (6, 2, 6);
insert into lecturers_to_courses values (6, 2, 7);
insert into lecturers_to_courses values (7, 2, 4);
insert into lecturers_to_courses values (7, 2, 5);
insert into lecturers_to_courses values (7, 2, 6);
insert into lecturers_to_courses values (7, 2, 7);
insert into lecturers_to_courses values (8, 2, 4);  
insert into lecturers_to_courses values (8, 2, 5);  
insert into lecturers_to_courses values (8, 2, 6);  
insert into lecturers_to_courses values (8, 2, 7);   
insert into lecturers_to_courses values (9, 2, 4);  
insert into lecturers_to_courses values (9, 2, 5);  
insert into lecturers_to_courses values (9, 2, 6);  
insert into lecturers_to_courses values (9, 2, 7);   
insert into lecturers_to_courses values (10, 2, 4);  
insert into lecturers_to_courses values (10, 2, 5);  
insert into lecturers_to_courses values (10, 2, 6);  
insert into lecturers_to_courses values (10, 2, 7);   


