package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.dao.CourseDao;
import rest.dao.ModuleDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Course;
import rest.model.datastructures.CourseTimesAndRooms;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Times;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/courses")
public class CourseResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@POST
	@Path("/addcourse/")
	@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED + ";charset=utf-8")
	public void addCourse(@FormParam("moduleid") int moduleId, @FormParam("courseid") Integer courseId,  @FormParam("modulepartsdata") String modulePartsData,
			@FormParam("coursedescription") String courseDescription, @FormParam("vvznr") String vvzNr, @FormParam("nrofgroups") int nrOfGroups,
			@FormParam("nrofstudentsexpectedpergroup") int nrOfStudentsExpectedPerGroup,
			@FormParam("ismaxnrofstudentsexpectedpergroup") boolean isMaxNrOfStudentsExpectedPerGroup,
			@FormParam("lecturersdata") String lecturersData, @FormParam("swstotpergroup") float swsTotalPerGroup,
			@FormParam("begindate") String beginDate, @FormParam("enddate") String endDate, @FormParam("rythm") String rythm,
			@FormParam("comments") String comments, @FormParam("roomsandtimesdata") String roomsAndTimesData,
			@FormParam("coursetype") String courseTypeData, @Context HttpServletResponse servletResponse

	) throws SQLException, IOException {
		try {
			Course course = new Course();
			// System.out.println(moduleId);
			// System.out.println(modulePartsData);
			// System.out.println(courseDescription);
			// System.out.println(nrOfGroups);
			// System.out.println(nrOfStudentsExpectedPerGroup);
			// System.out.println(isMaxNrOfStudentsExpectedPerGroup);
			// System.out.println(lecturersData);
			// System.out.println(swsTotalPerGroup);
			// System.out.println(beginDate);
			// System.out.println(endDate);
			// System.out.println(rythm);
			// System.out.println(comments);
			// System.out.println(isMaxNrOfStudentsExpectedPerGroup);
			// System.out.println(roomsAndTimesData);
			// System.out.println(courseTypeData);
			//
			
			Integer actualCourseId = courseId;
			if (actualCourseId == null) {
				System.out.println(CourseDao.instance.getNextCourseId());
				actualCourseId = CourseDao.instance.getNextCourseId();
			}
			System.out.println("Course id as param: " +courseId);
			System.out.println("Actual course id: " + actualCourseId);
			course.setId(actualCourseId);
			course.setModuleNr(moduleId);
 			course.setVvzNr(vvzNr);
			course.setNrOfGroups(nrOfGroups);
			course.setNrOfStudentsExpectedPerGroup(nrOfStudentsExpectedPerGroup);
			course.setIsMaxNrStudentsExpectedPerGroup(isMaxNrOfStudentsExpectedPerGroup);
			course.setSwsTotalPerGroup(swsTotalPerGroup);
			course.setStartDate(beginDate);
			course.setEndDate(endDate);
			course.setRythm(rythm);
			course.setComments(comments);
			course.setCourseType(StaticDataDao.instance.getCourseType(courseTypeData));
			course.setCourseDescription(courseDescription);

			ArrayList<Employee> lecturers = new ArrayList<Employee>();
			String[] lecturerIds = lecturersData.split(StaticDataDao.instance.outerSplittingPattern);
			for (String lecturerId : lecturerIds) {
				lecturerId = lecturerId.trim();
				if (lecturerId.length() > 0) {
					Employee employee = new Employee();
					employee.setId(Integer.parseInt(lecturerId));
					lecturers.add(employee);
				}
			}
			course.setLecturers(lecturers);

			ArrayList<CourseTimesAndRooms> timesAndRooms = new ArrayList<CourseTimesAndRooms>();
			String[] timesAndRoomsData = roomsAndTimesData.split(StaticDataDao.instance.outerSplittingPattern);
			System.out.println("Times and Rooms: " + timesAndRoomsData.length);
			for (String timesAndRoom : timesAndRoomsData) {
				System.out.println(timesAndRoom);

			}
			for (String timesAndRoom : timesAndRoomsData) {

				if (timesAndRoom.length() > 0) {

					String[] items = timesAndRoom.split(StaticDataDao.instance.innerSplittingPattern);
					CourseTimesAndRooms cTR = new CourseTimesAndRooms();

					if(items[0].trim().length() > 0) {
						try{
							cTR.setId(Integer.parseInt(items[0].trim()));
						}catch(NumberFormatException e){
							cTR.setId(CourseDao.instance.getNextCourseRoomsAndTimesId());
						}
					}
					cTR.setCourseId(course.getId());
					if (items[1].trim().length() > 0) {
						cTR.setDayOfWeek(items[1].trim());
					}
					Time startTime = null;
					if (items[2].trim().length() > 0) {
						String[] startTimes = items[2].trim().split(":");
						if (startTimes.length > 1) {
							int startHour = 0;
							try {
								startHour = Integer.parseInt(startTimes[0]);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							int startMin = 0;
							try {
								startMin = Integer.parseInt(startTimes[1]);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							startTime = new Time(startHour, startMin, 0);
						}
					}
					Time endTime = null;
					System.out.println("CourseResource:addCourse:endTime: " + items[2]);
					if (items[3].trim().length() > 0) {
						String[] endTimes = items[3].trim().split(":");
						if (endTimes.length > 1) {
							int endHour = 0;
							try {
								endHour = Integer.parseInt(endTimes[0]);
							} catch (NumberFormatException e) {

								// e.printStackTrace();
							}
							int endMin = 0;
							try {
								endMin = Integer.parseInt(endTimes[1]);
							} catch (NumberFormatException e) {

								// e.printStackTrace();
							}
							endTime = new Time(endHour, endMin, 0);
						}
					}

					cTR.setTimes(new Times(startTime, endTime));
					if (items[4].trim().length() > 0) {
						cTR.setRoomLabel(items[4].trim());
					}
					if (items[5].trim().length() > 0) {
						try {
							cTR.setRoomCapacity(Integer.parseInt(items[5].trim()));
						} catch (NumberFormatException e) {
							// e.printStackTrace();
							cTR.setRoomCapacity(-1);
						}
					}
					if (items[6].trim().length() > 0) {
						cTR.setComments(items[6]);
					}
					cTR.setModuleId(moduleId);
					timesAndRooms.add(cTR);
				}

			}
			course.setCourseTimesAndRooms(timesAndRooms);

			ArrayList<String> moduleParts = new ArrayList<>();
			String[] splittedModuleParts = modulePartsData.split(StaticDataDao.instance.outerSplittingPattern);
			for (String mP : splittedModuleParts) {
				moduleParts.add(mP);
			}
			course.setModuleParts(moduleParts);
			
			if(courseId == null) {
				CourseDao.instance.addCourse(course);
			}else{
				CourseDao.instance.updateCourse(course);
			}
			servletResponse.sendRedirect("../../showCourses.html?moduleid=" + moduleId);
		} catch (Exception e) {
			e.printStackTrace();
			servletResponse.sendRedirect("../../error.html");
		}
	}

	@GET
	@Path("/coursedetails/{moduleid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ArrayList<Course> getCoursesForModule(@PathParam("moduleid") int moduleId) {
		System.out.println("Module id in getCoursesForModule: " + moduleId);
		try {
			ArrayList<Course> coursesForModule = CourseDao.instance.getCoursesForModule(moduleId);
			System.out.println(coursesForModule);
			return coursesForModule;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@GET
	@Path("/coursedetails/{moduleid}/{courseid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Course getCourseDetails(@PathParam("moduleid") int moduleId, @PathParam("courseid") int courseId) {
		System.out.println("CourseResource::getCourseDetails: moduleId : " + moduleId + ", courseid: " + courseId);
		Course course = null;
		try {
			course = CourseDao.instance.getCourseDetails(moduleId, courseId);
			if (course != null) {
				System.out.println("==========================================");
				System.out.println("CourseResource::getCourseDetails::found course:");
				System.out.println(course);
				System.out.println("==========================================");
			} else {
				System.out.println("Could not find course with moduleId " + moduleId + " & course id " + courseId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return course;
	}

	@DELETE
	@Path("/deletecourse/{moduleid}/{courseid}")
	public boolean deletePlan(@PathParam("moduleid") int moduleId, @PathParam("courseid") int courseId) throws IOException {

		try {
			System.out.println("Delete course: moduleid " + moduleId + ", courseid: " + courseId);
			return CourseDao.instance.deleteCourse(moduleId, courseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@GET
	@Path("/nextroomid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public int nextRoomId() { 
		 return CourseDao.instance.getNextCourseRoomsAndTimesId();
	}

}