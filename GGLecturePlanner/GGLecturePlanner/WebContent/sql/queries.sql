Select * from courses c;
Select * from courses_times_and_rooms where course_id_fk=1;
Select distinct lecturer_fk from lecturers_to_courses where course_id_fk=1 and module_id_fk=1;
Select * from courses where module_id_fk=1;
select * from course_types;
select * from module_nrs where module_id_fk=2;
select * from courses;

