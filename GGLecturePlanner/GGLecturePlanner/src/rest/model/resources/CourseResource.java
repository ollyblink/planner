package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
import rest.model.datastructures.Course;

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
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addCourse(@FormParam("planyear") int planYear, @Context HttpServletResponse servletResponse

	) throws IOException {
		try {

			throw new SQLException();
		} catch (SQLException e) {
			e.printStackTrace();
			servletResponse.sendRedirect("../../error.html"); 
		}
	}

	@GET
	@Path("/coursedetails/{moduleid}")
	@Produces(MediaType.APPLICATION_JSON)
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
}