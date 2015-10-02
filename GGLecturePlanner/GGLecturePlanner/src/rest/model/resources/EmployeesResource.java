package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import com.sun.research.ws.wadl.Application;

import rest.dao.EmployeeDao;
import rest.dao.MessageDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Employee;
import rest.model.datastructures.ResponseMessage;
import rest.model.datastructures.Role;
import rest.model.datastructures.TrueFalseTupel;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/employees")
public class EmployeesResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("/allemployees")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ArrayList<Employee> getEmployees() {
		try {
			return EmployeeDao.instance.getEmployees();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@GET
	@Path("/alllecturers")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ArrayList<Employee> getLecturers() {
		try {
			return EmployeeDao.instance.getLecturers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@GET
	@Path("/employeedetails/{employeeid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Employee getLecturer(@PathParam("employeeid") int employeeId) {
		try {
			return EmployeeDao.instance.getEmployeeDetails(employeeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Employee();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/addemployee")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addEmployee(Map<String, Object> employeeDetails,
	// @FormParam("employeenr") String employeeNr, @FormParam("firstname") String firstName,
	// @FormParam("lastname") String lastName, @FormParam("email") String email, @FormParam("internalcostcenter") Integer internalCostCenter,
	// @FormParam("externalinstitute") String externalInstitute, @FormParam("ispaidseparately") Boolean isPaidSeparately,
	// @FormParam("username") String username, @FormParam("comments") String comments, @FormParam("roles") List<String> roles,
	// @FormParam("employeeid") Integer employeeId,
			@Context HttpServletResponse servletResponse) throws IOException {

		try {

			Employee employee = new Employee();
			try {
				employee.setId(Integer.parseInt((String) employeeDetails.get("employeeid")));
			} catch (NumberFormatException n) {
				employee.setId(null);
			}
			employee.setEmployeeNr(((String) employeeDetails.get("employeenr")));
			employee.setFirstName(((String) employeeDetails.get("firstname")));
			employee.setLastName(((String) employeeDetails.get("lastname")));
			employee.setEmail(((String) employeeDetails.get("email")));
			employee.setInternalCostCenter(((Integer) employeeDetails.get("internalcostcenter")));
			employee.setExternalInstitute(((String) employeeDetails.get("externalinstitute")));
			System.out.println(employeeDetails.keySet());
			System.out.println(employeeDetails.values());
			try {
				boolean trueFalse = ((Boolean) employeeDetails.get("ispaidseparately"));
				employee.setIsPaidSeparately(new TrueFalseTupel(trueFalse, (trueFalse ? "Ja" : "Nein")));
			} catch (NullPointerException n) {
				employee.setIsPaidSeparately(null);
			}
			employee.setUsername(((String) employeeDetails.get("username")));
			employee.setComments(((String) employeeDetails.get("comments")));

			System.out.println(employeeDetails.get("employeeroles"));
			System.out.println(employeeDetails.get("employeeroles").getClass().getSimpleName());
			try {
				employee.setRoles(getRealRoles(((ArrayList<LinkedHashMap<String, String>>) employeeDetails.get("employeeroles"))));
			} catch (NullPointerException n) {
				employee.setRoles(null);
			}

			if (employee.getId() == null) {
				Integer id = EmployeeDao.instance.getNextEmployeeId();
				employee.setId(id);
				String pw = EmployeeDao.instance.addEmployee(employee);
				MessageDao.instance.sendMessage(employee, pw);
				return Response.ok(new ResponseMessage("Created employee with id " + id, "ok")).build();
			} else {
				EmployeeDao.instance.updateEmployee(employee);
				return Response.ok(new ResponseMessage("Updated employee with id " + employee.getId(), "ok")).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

	@DELETE
	@Path("/deleteemployee/{employeeid}")
	public Response deletePlan(@PathParam("employeeid") int employeeId) throws IOException {

		try {
			boolean deleted = EmployeeDao.instance.deleteEmployee(employeeId);
			return Response.ok(new ResponseMessage("deleted employee with id " + employeeId + "?: " + deleted, "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

	private ArrayList<Role> getRealRoles(ArrayList<LinkedHashMap<String, String>> arrayList) {
		ArrayList<Role> realRoles = new ArrayList<>();
		for (LinkedHashMap<String, String> r : arrayList) {
			realRoles.add(new Role(r.get("abbreviation"), r.get("description")));

		}
		return realRoles;
	}

	@GET
	@Path("/home")
	@Produces(MediaType.TEXT_HTML)
	public String getHomeHtml() {
		return "<div ng-show=\"canUpdate()\">		<ul>			<li><a href=\"#/plans\">Pläne/Module					anzeigen/ändern/hinzufügen</a></li>			<li><a href=\"#/employees\">Angestellten					anzeigen/ändern/hinzufügen</a></li>		</ul>	</div>	<div ng-show=\"!canUpdate()\">		<ul>			<li><a href=\"#/plans\">Meine Pläne/Module anzeigen</a></li>		</ul>	</div>";
	}
}