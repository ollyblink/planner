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

import rest.dao.PlanDao;
import rest.model.datastructures.Plan;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/plans")
public class PlansResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("/allplans")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Plan> getEmployees() {
		try {
			return PlanDao.instance.getAllPlans();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@GET
	@Path("/plandetails/{planid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Plan getPlanDetails(@PathParam("planid") int planid) {
		try {
 			return PlanDao.instance.getPlanDetailsFor(planid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Plan();
	}

	@POST
	@Path("/addplan/")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addPlan(@FormParam("semester") String semester, @FormParam("year") int year, @Context HttpServletResponse servletResponse

	) throws IOException {

		try {
			PlanDao.instance.addPlan(semester, year);
			servletResponse.sendRedirect("../../showPlans.html");
		} catch (SQLException e) {
			e.printStackTrace();
			servletResponse.sendRedirect("../../error.html");
		}

	}
}