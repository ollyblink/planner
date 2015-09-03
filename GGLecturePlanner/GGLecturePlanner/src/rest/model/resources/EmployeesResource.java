package rest.model.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

import rest.dao.ServiceDao;
import rest.model.datastructures.Employee;

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
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Employee> getEmployees() {
		return ServiceDao.instance.getEmployees();
	}

	@POST
	@Path("/addemployee/")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addEmployee(@FormParam("employeenr") String employeeNr, @FormParam("firstname") String firstName,
			@FormParam("lastname") String lastName, @FormParam("email") String email, @FormParam("internalcostcenter") String internalcostcenter,
			@FormParam("externalinstitute") String externalInstitute, @FormParam("isexternal") String isExternal,
			@FormParam("isexternalpaidseparately") String isExternalPaidSeparately, @FormParam("username") String username,
			@FormParam("password") String password, @FormParam("comments") String comments, @Context HttpServletResponse servletResponse)
			throws IOException {

		System.out.println("I am in EmployeeResource.addEmployee: employee_nr:"+employeeNr);
		boolean isExternalBool = false, isExternalPaidSeparatelyBool = false;
		if (isExternal.equalsIgnoreCase("ja")) {
			isExternalBool = true;
		}
		if (isExternalPaidSeparately.equalsIgnoreCase("ja")) {
			isExternalPaidSeparatelyBool = true;
		}
		Employee employee = new Employee();
//				Employee(employeeNr, firstName, lastName, email, Integer.parseInt(internalcostcenter), externalInstitute,
//				isExternalBool, isExternalPaidSeparatelyBool, username, password, comments);

		boolean employeeHasBeenAdded = ServiceDao.instance.addEmployee(employee);

		if (employeeHasBeenAdded) {
			servletResponse.sendRedirect("../../index.html");
		} else {
			servletResponse.sendRedirect("../../answer.html");
		}
	}

}