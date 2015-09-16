package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.dao.EmployeeDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Role;

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
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
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
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Employee> getLecturers() {
		try {
			return EmployeeDao.instance.getLecturers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	@POST
	@Path("/addemployee/")
	@Produces(MediaType.TEXT_HTML+";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED+";charset=utf-8")
	public void addEmployee(@FormParam("employeenr") String employeeNr, @FormParam("firstname") String firstName,
			@FormParam("lastname") String lastName, @FormParam("email") String email, @FormParam("internalcostcenter") Integer internalCostCenter,
			@FormParam("externalinstitute") String externalInstitute, @FormParam("ispaidseparately") Boolean isPaidSeparately,
			@FormParam("username") String username, @FormParam("comments") String comments, @FormParam("roles") List<String> roles,
			@Context HttpServletResponse servletResponse) throws IOException {

		try {
			if (internalCostCenter == null) {
				internalCostCenter = 0;
			}

			ArrayList<Role> realRoles = getRealRoles(roles);
			int id = EmployeeDao.instance.getNextEmployeeId();
			Employee employee = new Employee(id, employeeNr, firstName, lastName, email, internalCostCenter, externalInstitute, isPaidSeparately,
					username, null, comments, realRoles, null);

			EmployeeDao.instance.addEmployee(employee);
			servletResponse.sendRedirect("../../showEmployees.html");

		} catch (SQLException e) {
			e.printStackTrace();
			servletResponse.sendRedirect("../../error.html");
		}
	}

	private ArrayList<Role> getRealRoles(List<String> roles) {
		ArrayList<Role> realRoles = new ArrayList<>();
		for (String r : roles) {
			realRoles.add(StaticDataDao.instance.getRole(r));
		}
		return realRoles;
	}
}