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
import rest.model.resources.CourseResource;

public enum CourseDao {
	instance;

	private PreparedStatement insertIntoCourse;
	private PreparedStatement insertCourseTimesRooms;
	private PreparedStatement insertLecturersToCourses;
	private PreparedStatement insertCourseModuleParts;

	private PreparedStatement getCoursesForModule;
	private PreparedStatement getTimesAndRoomsForCourse;
	private PreparedStatement getLecturersForCourse;
	private PreparedStatement getCourseModuleParts;

	private PreparedStatement deleteCourse;
	private PreparedStatement updateCourse;
	private PreparedStatement deleteCoursesTimesAndRooms;
	private PreparedStatement deleteLecturersToCourses;
	private PreparedStatement deleteCourseModuleParts;

	private CourseDao() {
		try {
			this.getCoursesForModule = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select * from courses where module_id_fk=?;");

			this.getTimesAndRoomsForCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select * from courses_times_and_rooms where course_id_fk=? and module_id_fk = ?;");

			this.getLecturersForCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Select distinct lecturer_fk from lecturers_to_courses where course_id_fk=? and module_id_fk=?;");
			this.getCourseModuleParts = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select distinct module_part from course_module_parts where course_id_fk=? and module_id_fk = ?;");

			this.insertIntoCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into courses values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			this.insertCourseTimesRooms = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into courses_times_and_rooms values(?,?,?,?,?,?,?,?,?);");
			this.insertLecturersToCourses = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into lecturers_to_courses values (?,?,?)");
			this.insertCourseModuleParts = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into course_module_parts values(?,?,?);");

			this.updateCourse = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"update courses set " + "course_description=?, " + "vvznr = ?, " + "nr_of_groups = ?, "
									+ "nr_of_students_expected_per_group = ?, " + "is_max_nr_students_expected_per_group = ?, "
									+ "sws_tot_per_group = ?, " + " date=?, " + "start_date = ?, " + "end_date = ?, " + "rythm = ?, "
									+ "comments = ?, " + "course_type_fk = ?" + "where " + "id = ? " + "and " + "module_id_fk=?" + ";");

			this.deleteCourse = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("delete from courses where module_id_fk=? and id = ?;");

			this.deleteCoursesTimesAndRooms = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("delete from courses_times_and_rooms where id=? and course_id_fk=? and module_id_fk=?;");

			this.deleteLecturersToCourses = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("delete from lecturers_to_courses where course_id_fk=? and module_id_fk=? and lecturer_fk = ?;");

			this.deleteCourseModuleParts = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("delete from course_module_parts where course_id_fk=? and module_id_fk=? and module_part = ?;");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addCourse(Course course) throws SQLException {

		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);

		insertIntoCourses(course);
		insertIntoTimesAndRooms(course);
		insertIntoLecturersToCourses(course);
		insertIntoCourseModuleParts(course);

		DBConnectionProvider.instance.getDataSource().getConnection().commit();
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);

	}

	public void updateCourse(Course updatedCourse) throws SQLException {
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);
		Course oldCourse = getCourseDetails(updatedCourse.getModuleNr(), updatedCourse.getId());

		updateCourses(updatedCourse);

		updateTimesAndRooms(updatedCourse, oldCourse);
		updateLecturersToCourses(updatedCourse, oldCourse);
		updateCourseModuleParts(updatedCourse, oldCourse);

		DBConnectionProvider.instance.getDataSource().getConnection().commit();
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);
	}

	private void updateCourses(Course course) throws SQLException {
		updateCourse.setString(1, course.getCourseDescription());
		updateCourse.setString(2, course.getVvzNr());
		updateCourse.setInt(3, course.getNrOfGroups());
		updateCourse.setInt(4, course.getNrOfStudentsExpectedPerGroup());
		updateCourse.setBoolean(5, course.getIsMaxNrStudentsExpectedPerGroup());
		updateCourse.setFloat(6, course.getSwsTotalPerGroup());
		updateCourse.setString(7, course.getDate());
		updateCourse.setDate(8, course.getStartDate());
		updateCourse.setDate(9, course.getEndDate());
		updateCourse.setString(10, course.getRythm());
		updateCourse.setString(11, course.getComments());
		String courseType = null;
		if (course.getCourseType() != null) {
			courseType = course.getCourseType().getAbbreviation();
		}
		updateCourse.setString(12, courseType);

		updateCourse.setInt(13, course.getId());
		updateCourse.setInt(14, course.getModuleNr());

		updateCourse.executeUpdate();
	}

	private void updateTimesAndRooms(Course updatedCourse, Course oldCourse) throws SQLException {
		deleteFromTimesAndRooms(oldCourse);
		insertIntoTimesAndRooms(updatedCourse);
	}

	private void deleteFromTimesAndRooms(Course oldCourse) throws SQLException {
		ArrayList<CourseTimesAndRooms> timesAndRooms = oldCourse.getCourseTimesAndRooms();
		for (CourseTimesAndRooms t : timesAndRooms) {
			deleteCoursesTimesAndRooms.setInt(1, t.getId());
			deleteCoursesTimesAndRooms.setInt(2, t.getCourseId());
			deleteCoursesTimesAndRooms.setInt(3, t.getModuleId());
			deleteCoursesTimesAndRooms.executeUpdate();
		}
	}

	private void updateLecturersToCourses(Course updatedCourse, Course oldCourse) throws SQLException {
		deleteLecturersToCourses(oldCourse);
		insertIntoLecturersToCourses(updatedCourse);
	}

	private void deleteLecturersToCourses(Course oldCourse) throws SQLException {
		ArrayList<Employee> lecturers = oldCourse.getLecturers();
		for (Employee e : lecturers) {
			this.deleteLecturersToCourses.setInt(1, oldCourse.getId());
			this.deleteLecturersToCourses.setInt(2, oldCourse.getModuleNr());
			this.deleteLecturersToCourses.setInt(3, e.getId());
			this.deleteLecturersToCourses.executeUpdate();
		}
	}

	private void updateCourseModuleParts(Course updatedCourse, Course oldCourse) throws SQLException {
		deleteCourseModuleParts(oldCourse);
		insertIntoCourseModuleParts(updatedCourse);
	}

	private void deleteCourseModuleParts(Course oldCourse) throws SQLException {
		ArrayList<String> moduleParts = oldCourse.getModuleParts();
		for (String m : moduleParts) {
			this.deleteCourseModuleParts.setInt(1, oldCourse.getId());
			this.deleteCourseModuleParts.setInt(2, oldCourse.getModuleNr());
			this.deleteCourseModuleParts.setString(3, m);
			this.deleteCourseModuleParts.executeUpdate();
		}

	}

	private void insertIntoCourseModuleParts(Course course) throws SQLException {
		for (String modulePart : course.getModuleParts()) {
			this.insertCourseModuleParts.setInt(1, course.getId());
			this.insertCourseModuleParts.setInt(2, course.getModuleNr());
			this.insertCourseModuleParts.setString(3, modulePart);

			this.insertCourseModuleParts.executeUpdate();
		}
	}

	private void insertIntoLecturersToCourses(Course course) throws SQLException {
		for (Employee empl : course.getLecturers()) {
			this.insertLecturersToCourses.setInt(1, course.getId());
			this.insertLecturersToCourses.setInt(2, course.getModuleNr());
			this.insertLecturersToCourses.setInt(3, empl.getId());

			this.insertLecturersToCourses.executeUpdate();
		}
	}

	private void insertIntoTimesAndRooms(Course course) throws SQLException {
		for (CourseTimesAndRooms cTR : course.getCourseTimesAndRooms()) {
			this.insertCourseTimesRooms.setObject(1, cTR.getId());
			this.insertCourseTimesRooms.setObject(2, cTR.getCourseId());
			this.insertCourseTimesRooms.setObject(3, cTR.getModuleId());
			try {
				this.insertCourseTimesRooms.setObject(4, cTR.getTimes().getStartTime());
			} catch (Exception e) {
				this.insertCourseTimesRooms.setObject(4, null);
			}
			try {
				this.insertCourseTimesRooms.setObject(5, cTR.getTimes().getEndTime());
			} catch (Exception e) {
				this.insertCourseTimesRooms.setObject(5, null);
			}
			this.insertCourseTimesRooms.setString(6, cTR.getDayOfWeek());
			this.insertCourseTimesRooms.setObject(7, cTR.getRoomCapacity());
			this.insertCourseTimesRooms.setString(8, cTR.getRoomLabel());
			this.insertCourseTimesRooms.setString(9, cTR.getComments());
			this.insertCourseTimesRooms.executeUpdate();
		}
	}

	private void insertIntoCourses(Course course) throws SQLException {
		try {
			this.insertIntoCourse.setObject(1, course.getId());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(1, null);
		}
		this.insertIntoCourse.setString(2, course.getCourseDescription());
		try {
			this.insertIntoCourse.setObject(3, course.getModuleNr());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(3, null);
		}
		this.insertIntoCourse.setString(4, course.getVvzNr());
		try {
			this.insertIntoCourse.setObject(5, course.getNrOfGroups());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(5, null);
		}
		try {
			this.insertIntoCourse.setObject(6, course.getNrOfStudentsExpectedPerGroup());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(6, null);
		}
		try {
			this.insertIntoCourse.setObject(7, course.getIsMaxNrStudentsExpectedPerGroup());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(7, null);
		}
		try {
			this.insertIntoCourse.setObject(8, course.getSwsTotalPerGroup());
		} catch (Exception e) {
			this.insertIntoCourse.setObject(8, null);
		}
		this.insertIntoCourse.setString(9, course.getDate());
		this.insertIntoCourse.setDate(10, course.getStartDate());
		this.insertIntoCourse.setDate(11, course.getEndDate());
		this.insertIntoCourse.setString(12, course.getRythm());
		this.insertIntoCourse.setString(13, course.getComments());
		if (course.getCourseType() != null) {
			this.insertIntoCourse.setString(14, course.getCourseType().getAbbreviation());
		} else {
			this.insertIntoCourse.setString(14, null);
		}

		this.insertIntoCourse.executeUpdate();
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
			course.setDate(r.getString("date"));
			System.out.println("Course get Date() :  "+course.getDate());
			course.setStartDate(r.getDate("start_date"));
			course.setEndDate(r.getDate("end_date"));
			course.setRythm(r.getString("rythm"));
			course.setComments(r.getString("comments"));
			if (r.getString("course_type_fk") != null) {
				course.setCourseType(StaticDataDao.instance.getCourseType(r.getString("course_type_fk")));
			} else {
				course.setCourseType(null);
			}
			courses.add(course);
		}
		r.close();

		for (Course course : courses) {
			// Get all times and rooms for each course
			course.setCourseTimesAndRooms(getCourseTimesAndRooms(moduleId, course.getId()));
			// Get all lecturers for each course
			course.setLecturers(getLecturersForCourse(moduleId, course.getId()));
			// Get all the course module parts for each course
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

	public boolean deleteCourse(int moduleId, int courseId) throws SQLException {
		deleteCourse.setInt(1, moduleId);
		deleteCourse.setInt(2, courseId);
		int update = deleteCourse.executeUpdate();
		return (update > 0);
	}

	public Course getCourseDetails(int moduleId, int courseId) throws SQLException {
		ArrayList<Course> courses = getCoursesForModule(moduleId);
		for (Course course : courses) {
			if (course.getId().equals(courseId)) {
				return course;
			}
		}

		return null;
	}

	public void copyCourse(int moduleId, int courseId) throws SQLException {
		Course course = getCourseDetails(moduleId, courseId);
		course.setId(getNextCourseId());
		for(CourseTimesAndRooms cTR: course.getCourseTimesAndRooms()){
			cTR.setId(getNextCourseRoomsAndTimesId());
		}
		addCourse(course);
	}

}
