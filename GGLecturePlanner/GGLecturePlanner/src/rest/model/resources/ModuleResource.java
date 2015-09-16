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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.dao.EmployeeDao;
import rest.dao.ModuleDao;
import rest.dao.PlanDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Module;
import rest.model.datastructures.ModuleType;
import rest.model.datastructures.Plan;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/modules")
public class ModuleResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@POST
	@Path("/addmodule/")
	@Produces(MediaType.TEXT_HTML+";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED+";charset=utf-8")
	public void addModule(
			@FormParam("primarynr") String primaryNr,
			@FormParam("semesternr") String semesterNr, 
			@FormParam("assessmenttype") String assessmentType,
			@FormParam("assessmentdate") String assessmentDate, 
			@FormParam("moduletypes") List<String> moduleTypes,
			@FormParam("responsibleemployee") int responsibleEmployeeId,
			@FormParam("comments") String comments, 
			@FormParam("disciplines") List<String> disciplines, 
			@FormParam("department") String department,
			@FormParam("planid") int planId, 
			@FormParam("plansemester") String planSemester, 
			@FormParam("planyear") int planYear, 
			@Context HttpServletResponse servletResponse

	) throws IOException { 
 		ArrayList<String> primaryNrs = new ArrayList<>();
		String[] pNs = primaryNr.split(";");
		for(String pN: pNs){
			primaryNrs.add(pN);
		}
		ArrayList<ModuleType> moduleTypesReal = new ArrayList<>();
		for(String type: moduleTypes){
			moduleTypesReal.add(StaticDataDao.instance.getModuleType(type));
		}
		
		ArrayList<Discipline> disciplineReal = new ArrayList<>();
		for(String type: disciplines){
			disciplineReal.add(StaticDataDao.instance.getDiscipline(type));
		}
		 
		
		try {
			Employee responsibleEmployee = new Employee();
			responsibleEmployee.setId(responsibleEmployeeId);
			
			Module module = new Module(
					ModuleDao.instance.getNextModuleId(),
					primaryNrs, 
					semesterNr, 
					StaticDataDao.instance.getAssessmentType(assessmentType), 
					assessmentDate, 
					responsibleEmployee,
					comments, 
					null, 
					moduleTypesReal, 
					disciplineReal, 
					StaticDataDao.instance.getDepartment(department)
			);
			
			System.out.println("Adding module: " + module);
			ModuleDao.instance.addModule(planId, module);
					
			servletResponse.sendRedirect("../../showModules.html?planid="+planId+"&semester="+planSemester+"&year="+planYear);

		} catch (SQLException e) {
			e.printStackTrace();
			servletResponse.sendRedirect("../../error.html");

		} 
	}
	
	@GET
	@Path("/moduledetails/{moduleid}")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public Module getModuleDetailsFor(@PathParam("moduleid") int moduleId){
		try {
			return ModuleDao.instance.getModuleDetails(moduleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Module();
	}
	 
}