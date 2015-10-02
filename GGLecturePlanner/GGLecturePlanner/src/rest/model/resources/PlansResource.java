package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import rest.dao.PlanDao;
import rest.model.datastructures.Plan;
import rest.model.datastructures.ResponseMessage;

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
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ArrayList<Plan> getAllPlans() {
		try {
			return PlanDao.instance.getAllPlans();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@GET
	@Path("/plandetails/{planid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Plan getPlanDetails(@PathParam("planid") int planId) {
		try {
			return PlanDao.instance.getPlanDetailsFor(planId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@DELETE
	@Path("/deleteplan/{planid}")
	public Response deletePlan(@PathParam("planid") int planId) throws IOException {

		try {
			PlanDao.instance.deletePlan(planId);
			return Response.ok(new ResponseMessage("deleted plan: " + planId, "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();

			return Response.status(Status.NOT_MODIFIED).build();
		}
	}

	@POST
	@Path("/addplan")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addPlan(Map<String, String> plans, @Context HttpServletResponse servletResponse

	) throws IOException {
		try {
			System.out.println("Adding plan: " + plans.get("semester") + ", " + Integer.parseInt(plans.get("year")));
			int id = PlanDao.instance.addPlan(plans.get("semester"), Integer.parseInt(plans.get("year")));
			return Response.ok(
					new ResponseMessage("Created new plan with id: " + id + ", semester: " + plans.get("semester") + ", year "
							+ Integer.parseInt(plans.get("year")), "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

	@POST
	@Path("/changeplan")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changePlan(Map<String, String> plans, @Context HttpServletResponse servletResponse

	) throws IOException {

		try {
			PlanDao.instance.changePlan(Integer.parseInt(plans.get("id")), plans.get("semester"), Integer.parseInt(plans.get("year")));
			return Response.ok(
					new ResponseMessage("Changed plan with id: " + plans.get("id") + ", semester: " + plans.get("semester") + ", year "
							+ Integer.parseInt(plans.get("year")), "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();
		}
	}

	@GET
	@Path("/copyplan/{planid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response copyPlan(@PathParam("planid") int planId, @Context HttpServletResponse servletResponse) throws IOException {
		try {
			PlanDao.instance.copyPlan(planId);
			return Response.ok(new ResponseMessage("copied plan: " + planId, "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();
		}
	}

	@GET
	@Path("/printplan/{planid}")
	@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	public String printPlan(@PathParam("planid") int planId, @Context HttpServletResponse servletResponse) throws IOException {

		try {
			return PlanDao.instance.createHTMLPage(planId); // TODO
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "<html><head></head><body><p>Something went wrong! :(</p></body></html>";

	}
}