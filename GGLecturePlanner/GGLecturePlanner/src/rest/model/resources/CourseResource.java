package rest.model.resources;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import rest.dao.CourseDao;
import rest.dao.EmployeeDao;
import rest.dao.PlanDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Course;
import rest.model.datastructures.CourseTimesAndRooms;
import rest.model.datastructures.Employee;
import rest.model.datastructures.ResponseMessage;
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

	@SuppressWarnings({ "unchecked", "deprecation" })
	@POST
	@Path("/addcourse/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addCourse(Map<String, Object> courseData, @Context HttpServletResponse servletResponse) throws SQLException, IOException {

		try {
			Course course = new Course();

			Integer actualCourseId = null;
			Integer courseId = null;
			try {
				courseId = (Integer) courseData.get("courseid");
			} catch (ClassCastException e) {
				try {
					courseId = Integer.parseInt((String) courseData.get("courseid"));
				} catch (NumberFormatException n) {
					courseId = null;
				}
			}
			if (courseId == null) {
				actualCourseId = CourseDao.instance.getNextCourseId();
			} else {
				actualCourseId = courseId;
			}
			course.setId(actualCourseId);

			try {
				course.setModuleNr((Integer) courseData.get("moduleid"));
			} catch (ClassCastException e) {
				try {
					course.setModuleNr(Integer.parseInt((String) courseData.get("moduleid")));
				} catch (NumberFormatException n) {
					course.setModuleNr(null);
				}
			}
			try {
				course.setVvzNr((String) courseData.get("vvznr"));
			} catch (Exception e) {
				course.setVvzNr(null);
			}
			try {
				course.setNrOfGroups((Integer) courseData.get("nrofgroups"));
			} catch (Exception e) {
				course.setNrOfGroups(null);
			}

			try {
				course.setNrOfStudentsExpectedPerGroup((Integer) courseData.get("nrofstudentsexpectedpergroup"));
			} catch (Exception e) {
				course.setNrOfStudentsExpectedPerGroup(null);
			}
			try {
				course.setIsMaxNrStudentsExpectedPerGroup((Boolean) courseData.get("ismaxnrofstudentsexpectedpergroup"));
			} catch (Exception e) {
				course.setIsMaxNrStudentsExpectedPerGroup(null);
			}
			try {
				course.setSwsTotalPerGroup((float) ((int) courseData.get("swstotpergroup")));
			} catch (Exception e) {
				course.setSwsTotalPerGroup(null);
			}
			try {
				course.setDate(""+courseData.get("date"));
			} catch (Exception e) {
				e.printStackTrace();
				course.setDate(null);
			} 
			System.out.println("Begindate:" + courseData.get("begindate"));
			System.out.println("Enddate:" + courseData.get("enddate"));
			System.out.println("Date:" + courseData.get("date"));

			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			try { 
				course.setStartDate(new Date(df.parse(((String) courseData.get("begindate"))).getTime()));
			} catch (Exception e) { 
				course.setStartDate(null);
			}
			try { 
				course.setEndDate(new Date(df.parse(((String) courseData.get("enddate"))).getTime()));
			} catch (Exception e) { 
				course.setEndDate(null);
			}
			System.out.println("Course start date: " + course.getStartDate());
			System.out.println("Course end date: " + course.getEndDate());
			try {
				course.setRythm((String) courseData.get("rythm"));
			} catch (Exception e) {
				course.setRythm(null);
			}
			try {
				course.setComments((String) courseData.get("comments"));
			} catch (Exception e) {
				course.setComments(null);
			}
			try {
				course.setCourseType(StaticDataDao.instance.getCourseType((String) courseData.get("coursetype")));
			} catch (Exception e) {
				course.setCourseType(null);
			}
			try {
				course.setCourseDescription((String) courseData.get("coursedescription"));
			} catch (Exception e) {
				course.setCourseDescription(null);
			}

			ArrayList<Employee> lecturers = new ArrayList<>();
			ArrayList<Map<String, Object>> lids = (ArrayList<Map<String, Object>>) courseData.get("selectedlecturers");
			for (Map<String, Object> lect : lids) {
				Employee employee = EmployeeDao.instance.getEmployeeDetails((Integer) lect.get("id"));
				lecturers.add(employee);
			}
			course.setLecturers(lecturers);
			course.setModuleParts((ArrayList<String>) courseData.get("moduleparts"));

			ArrayList<CourseTimesAndRooms> courseTimesAndRooms = new ArrayList<>();
			ArrayList<Map<String, Object>> tRs = (ArrayList<Map<String, Object>>) courseData.get("roomsandtimes");
			for (Map<String, Object> tR : tRs) {
				CourseTimesAndRooms cTR = new CourseTimesAndRooms();
				Object idObject = tR.get("id");
				try {
					cTR.setId((Integer) idObject);
				} catch (ClassCastException e) {
					try {
						cTR.setId(Integer.parseInt((String) idObject));
					} catch (NumberFormatException n) {
						cTR.setId(null);
					}
				}
				cTR.setComments((String) tR.get("roomcomments"));
				cTR.setCourseId(course.getId());
				cTR.setModuleId(course.getModuleNr());
				cTR.setDayOfWeek((String) tR.get("dayofweek"));
				Object roomCapacityObject = tR.get("roomcapacity");
				try {
					cTR.setRoomCapacity((Integer) roomCapacityObject);
				} catch (ClassCastException e) {
					try {
						cTR.setRoomCapacity(Integer.parseInt((String) roomCapacityObject));
					} catch (NumberFormatException n) {
						cTR.setRoomCapacity(null);
					}
				}
				cTR.setRoomLabel((String) tR.get("roomlabel"));

				Times times = new Times();
				Integer startHour, startMin, startSec = 0, endHour, endMin, endSec = 0;
				try {
					String[] startParts = ((String) tR.get("begintime")).split(":");
					try {
						startHour = Integer.parseInt(startParts[0]);
					} catch (Exception e) {
						startHour = null;
					}
					try {
						startMin = Integer.parseInt(startParts[1]);
					} catch (Exception e) {
						startMin = null;
					}
					if (startHour != null && startMin != null) {
						times.setStartTime(new Time(startHour, startMin, startSec));
					}
				} catch (NullPointerException e) {
					times.setStartTime(null);
				}
				try {
					String[] endParts = ((String) tR.get("endtime")).split(":");
					try {
						endHour = Integer.parseInt(endParts[0]);
					} catch (Exception e) {
						endHour = null;
					}
					try {
						endMin = Integer.parseInt(endParts[1]);
					} catch (Exception e) {
						endMin = null;
					}

					if (endHour != null && endMin != null) {
						times.setStartTime(new Time(endHour, endMin, endSec));
					}
				} catch (NullPointerException e) {
					times.setEndTime(null);
				}
				cTR.setTimes(times);
				// System.out.println("CTR: " + cTR);
				courseTimesAndRooms.add(cTR);
			}
			// System.out.println("Before insertion: " + courseData.get("roomsandtimes"));
			// System.out.println(courseTimesAndRooms);
			course.setCourseTimesAndRooms(courseTimesAndRooms);

			if (courseId == null) {
				CourseDao.instance.addCourse(course);
				return Response.ok(new ResponseMessage("added new course with id : " + course.getId(), "ok")).build();
			} else {
				CourseDao.instance.updateCourse(course);
				return Response.ok(new ResponseMessage("changed course with id : " + course.getId(), "ok")).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

	@GET
	@Path("/coursedetails/moduleid/{moduleid}")
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
	@Path("/coursedetails/moduleid/{moduleid}/courseid/{courseid}")
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
	@Path("/deletecourse/moduleid/{moduleid}/courseid/{courseid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteCourse(@PathParam("moduleid") int moduleId, @PathParam("courseid") int courseId) throws IOException {

		try {
			boolean deleted = CourseDao.instance.deleteCourse(moduleId, courseId);
			if (deleted) {
				return Response.ok(new ResponseMessage("Delete course: moduleid " + moduleId + ", courseid: " + courseId, "ok")).build();
			} else {
				return Response.status(Status.NOT_MODIFIED).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

	@GET
	@Path("/nextroomid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public int nextRoomId() {
		return CourseDao.instance.getNextCourseRoomsAndTimesId();
	}

	@GET
	@Path("/copycourse/moduleid/{moduleid}/courseid/{courseid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response copyPlan(@PathParam("moduleid") int moduleId, @PathParam("courseid") int courseId, @Context HttpServletResponse servletResponse) throws IOException {
		try {
			CourseDao.instance.copyCourse(moduleId, courseId);
			return Response.ok(new ResponseMessage("copied course: " + courseId, "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();
		}
	}


}