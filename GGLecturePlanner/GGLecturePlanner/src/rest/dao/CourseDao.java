package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Course;
import rest.model.datastructures.CourseTimesAndRooms;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Times;

public enum CourseDao {
	instance;

	private PreparedStatement getCoursesForModule;
	private PreparedStatement getTimesAndRoomsForCourse;
	private PreparedStatement getLecturersForCourse;
	private PreparedStatement insertIntoCourse;
	private PreparedStatement insertCourseTimesRooms;
	private PreparedStatement insertLecturersToCourses;
	private PreparedStatement insertCourseModuleParts;
	private PreparedStatement getCourseModuleParts;

	private CourseDao() {
		try {
			this.getCoursesForModule = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select * from courses where module_id_fk=?;");

			this.getTimesAndRoomsForCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select * from courses_times_and_rooms where course_id_fk=? and module_id_fk = ?;");

			this.getLecturersForCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select distinct lecturer_fk from lecturers_to_courses where course_id_fk=? and module_id_fk=?;");
			this.getCourseModuleParts = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("select distinct module_part from course_module_parts where course_id_fk=? and module_id_fk = ?;");

			this.insertIntoCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into courses values (?,?,?,?,?,?,?,?,?,?,?,?,?);");
			this.insertCourseTimesRooms = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into courses_times_and_rooms values(?,?,?,?,?,?,?,?,?);");
			this.insertLecturersToCourses = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into lecturers_to_courses values (?,?,?)");
			this.insertCourseModuleParts = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("insert into course_module_parts values(?,?,?);");

		} catch (SQLException e) { 
			e.printStackTrace();
		}

	}

	public void addCourse(Course course) throws SQLException {

		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);

		this.insertIntoCourse.setInt(1, course.getId());
		this.insertIntoCourse.setString(2, course.getCourseDescription());
		this.insertIntoCourse.setInt(3, course.getModuleNr());
		this.insertIntoCourse.setString(4, course.getVvzNr()); 
		this.insertIntoCourse.setInt(5, course.getNrOfGroups());
		this.insertIntoCourse.setInt(6, course.getNrOfStudentsExpectedperGroup());
		this.insertIntoCourse.setBoolean(7, course.isMaxNrStudentsExpectedPerGroup());
		this.insertIntoCourse.setFloat(8, course.getSwsTotalPerGroup()); 
		this.insertIntoCourse.setString(9, course.getStartDate());
		this.insertIntoCourse.setString(10, course.getEndDate());
		this.insertIntoCourse.setString(11, course.getRythm());
		this.insertIntoCourse.setString(12, course.getComments());
		this.insertIntoCourse.setString(13, course.getCourseType().getAbbreviation());
		
		this.insertIntoCourse.executeUpdate();

		for (CourseTimesAndRooms cTR : course.getCourseTimesAndRooms()) {
			System.out.println(cTR);
			this.insertCourseTimesRooms.setInt(1, cTR.getId());
			this.insertCourseTimesRooms.setInt(2, cTR.getCourseId());
			this.insertCourseTimesRooms.setInt(3, cTR.getModuleId());
			this.insertCourseTimesRooms.setTime(4, cTR.getTimes().getStartTime());
			this.insertCourseTimesRooms.setTime(5, cTR.getTimes().getEndTime());
			this.insertCourseTimesRooms.setString(6, cTR.getDayOfWeek());
			this.insertCourseTimesRooms.setInt(7, cTR.getRoomCapacity());
			this.insertCourseTimesRooms.setString(8, cTR.getRoomLabel());
			this.insertCourseTimesRooms.setString(9, cTR.getComments());

			this.insertCourseTimesRooms.executeUpdate();
		}

		for (Employee empl : course.getLecturers()) {
			this.insertLecturersToCourses.setInt(1, course.getId());
			this.insertLecturersToCourses.setInt(2, course.getModuleNr());
			this.insertLecturersToCourses.setInt(3, empl.getId());
			
			this.insertLecturersToCourses.executeUpdate();
		}
		
		for(String modulePart: course.getModuleParts()){
			this.insertCourseModuleParts.setInt(1, course.getId());
			this.insertCourseModuleParts.setInt(2, course.getModuleNr());
			this.insertCourseModuleParts.setString(3, modulePart);
			
			this.insertCourseModuleParts.executeUpdate();
		}

		DBConnectionProvider.instance.getDataSource().getConnection().commit();
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);

	}

	public int getNextCourseId() {

		int nextId = -1;
		try {
			Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r = s.executeQuery("select nextval('courses_id_seq');");
			while (r.next()) {
				nextId = r.getInt("nextval");
			}
			r.close();
			s.close();
		} catch (SQLException s) {
			s.getStackTrace();
		}
		return nextId;
	}

	public int getNextCourseRoomsAndTimesId() {

		int nextId = -1;
		try {
			Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r = s.executeQuery("select nextval('courses_times_and_rooms_id_seq');");
			while (r.next()) {
				nextId = r.getInt("nextval");
			}
			r.close();
			s.close();
		} catch (SQLException s) {
			s.getStackTrace();
		}
		return nextId;
	}

	public ArrayList<Course> getCoursesForModule(int moduleId) throws SQLException {

		ArrayList<Course> courses = new ArrayList<Course>();

		// Get all courses per module
		getCoursesForModule.setInt(1, moduleId);
		ResultSet r = getCoursesForModule.executeQuery();
		while (r.next()) {
			Course course = new Course();
			course.setId(r.getInt("id")); 
			course.setCourseDescription(r.getString("course_description"));
			course.setModuleNr(r.getInt("module_id_fk"));
			course.setVvzNr(r.getString("vvznr"));
			course.setNrOfGroups(r.getInt("nr_of_groups"));
			course.setNrOfStudentsExpectedPerGroup(r.getInt("nr_of_students_expected_per_group"));
			course.setIsMaxNrStudentsExpectedPerGroup(r.getBoolean("is_max_nr_students_expected_per_group"));
			course.setSwsTotalPerGroup(r.getFloat("sws_tot_per_group")); 
			course.setStartDate(r.getString("start_date"));
			course.setEndDate(r.getString("end_date"));
			course.setRythm(r.getString("rythm"));
			course.setComments(r.getString("comments"));
			course.setCourseType(StaticDataDao.instance.getCourseType(r.getString("course_type_fk")));
			courses.add(course);
		}
		r.close();

		for (Course course : courses) {
			// Get all times and rooms for each course
			course.setCourseTimesAndRooms(getCourseTimesAndRooms(moduleId, course.getId()));
			// Get all lecturers for each course
			course.setLecturers(getLecturersForCourse(moduleId, course.getId()));
			//Get all the course module parts for each course
			course.setModuleParts(getModuleParts(moduleId, course.getId()));
		}
		return courses;
	}

	private ArrayList<String> getModuleParts(int moduleId, Integer courseId) throws SQLException {
		ArrayList<String> moduleParts = new ArrayList<String>();
		getCourseModuleParts.setInt(1, courseId);
		getCourseModuleParts.setInt(2, moduleId);

		ResultSet r = getCourseModuleParts.executeQuery();
		while (r.next()) { 

			moduleParts.add(r.getString(1));
		}
		r.close();

		return moduleParts;
	}

	private ArrayList<CourseTimesAndRooms> getCourseTimesAndRooms(Integer moduleId, Integer courseId) throws SQLException {
		ArrayList<CourseTimesAndRooms> timesAndRooms = new ArrayList<CourseTimesAndRooms>();
		getTimesAndRoomsForCourse.setInt(1, courseId);
		getTimesAndRoomsForCourse.setInt(2, moduleId);

		ResultSet r = getTimesAndRoomsForCourse.executeQuery();
		while (r.next()) {
			CourseTimesAndRooms cTR = new CourseTimesAndRooms();
			cTR.setId(r.getInt("id"));
			cTR.setCourseId(r.getInt("course_id_fk"));
			cTR.setModuleId(r.getInt("module_id_fk"));
			cTR.setTimes(new Times(r.getTime("start_time"), r.getTime("end_time")));
			cTR.setDayOfWeek(r.getString("day_of_week"));
			cTR.setRoomCapacity(r.getInt("room_capacity"));
			cTR.setRoomLabel(r.getString("room_label"));
			cTR.setComments(r.getString("comments"));

			timesAndRooms.add(cTR);
		}
		r.close();

		return timesAndRooms;
	}

	private ArrayList<Employee> getLecturersForCourse(Integer moduleId, Integer courseId) throws SQLException {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		getLecturersForCourse.setInt(1, courseId);
		getLecturersForCourse.setInt(2, moduleId);
		ResultSet r = getLecturersForCourse.executeQuery();

		while (r.next()) {
			employees.add(EmployeeDao.instance.getEmployeeFirstAndLastNameFor(r.getInt("lecturer_fk")));
		}
		r.close();

		return employees;
	}

}
