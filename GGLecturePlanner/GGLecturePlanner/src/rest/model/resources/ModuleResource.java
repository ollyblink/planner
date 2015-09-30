package rest.model.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import rest.dao.ModuleDao;
import rest.dao.StaticDataDao;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Module;
import rest.model.datastructures.ModuleType;
import rest.model.datastructures.ResponseMessage;

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

	@SuppressWarnings("unchecked")
	@POST
	@Path("/addmodule")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addModule(Map<String, Object> moduleDetails, @Context HttpServletResponse servletResponse

	// @FormParam("moduleprimarynrs") String modulePrimaryNrs, @FormParam("semesternr") String semesterNr,
	// @FormParam("assessmenttype") String assessmentType, @FormParam("assessmentdate") String assessmentDate,
	// @FormParam("moduletypes") List<String> moduleTypes, @FormParam("responsibleemployee") int responsibleEmployeeId,
	// @FormParam("comments") String comments, @FormParam("disciplines") List<String> disciplines, @FormParam("department") String department,
	// @FormParam("planid") int planId, @FormParam("moduleid") Integer moduleId

	) throws IOException {
		System.out.println("Keys: " + moduleDetails.keySet());
		System.out.println("Values: " + moduleDetails.values());
		ArrayList<String> primaryNrs = new ArrayList<String>(((List<String>) moduleDetails.get("modulePrimaryNrs")));
		// String[] pNs = ((String) ).split(StaticDataDao.instance.outerSplittingPattern);
		// for (String pN : pNs) {
		// primaryNrs.add(pN);
		// }
		ArrayList<ModuleType> moduleTypesReal = new ArrayList<>();
		List<String> mTypeAbbreviations = ((List<String>) moduleDetails.get("moduletypes"));
		for (String type : mTypeAbbreviations) {
			moduleTypesReal.add(StaticDataDao.instance.getModuleType(type));
		}
		System.out.println("Actual module types: " + moduleTypesReal);

		ArrayList<Discipline> disciplineReal = new ArrayList<>();

		List<String> discs = ((List<String>) moduleDetails.get("disciplines"));
		for (String type : discs) {
			disciplineReal.add(StaticDataDao.instance.getDiscipline(type));
		}
		System.out.println("Disciplines: " + disciplineReal);

		try {
			Employee responsibleEmployee = new Employee();

			try {
				responsibleEmployee.setId(Integer.parseInt((String) moduleDetails.get("responsibleemployee")));
			} catch (Exception e) {
				responsibleEmployee.setId(null);
			}

			Integer actualModuleId = null;
			try {
				actualModuleId = Integer.parseInt((String) moduleDetails.get("moduleid"));
			} catch (NumberFormatException e) {
				actualModuleId = null;
			}
			if (actualModuleId == null) {
				actualModuleId = ModuleDao.instance.getNextModuleId();
			}

			Module module = new Module();
			module.setId(actualModuleId);
			module.setPrimaryNrs(primaryNrs);
			module.setSemesterNr(moduleDetails.get("semesternr") + "");

			try {
				module.setAssessmentType(StaticDataDao.instance.getAssessmentType(((String) moduleDetails.get("assessmenttype"))));
			} catch (NullPointerException e) {
				module.setAssessmentType(null);
			}

			module.setAssessmentDate(((String) moduleDetails.get("assessmentdate")));
			module.setResponsibleEmployee(responsibleEmployee);

			module.setComments(((String) moduleDetails.get("comments")));
			module.setModuleTypes(moduleTypesReal);
			module.setDisciplines(disciplineReal);

			try {
				module.setDepartment(StaticDataDao.instance.getDepartment(((String) moduleDetails.get("department"))));
			} catch (NullPointerException e) {
				module.setDepartment(null);
			}
			Integer planId = null;
			try {
				planId = Integer.parseInt(((String) moduleDetails.get("planid")));
			} catch (NumberFormatException e) {
				planId = null;
			}
			if (moduleDetails.get("moduleid") == null) {
				System.out.println("Adding module: " + module);
				ModuleDao.instance.addModule(planId, module);
				return Response.ok(new ResponseMessage("Created module with id: " + module.getId(), "ok")).build();
			} else {
				System.out.println("Updating module: " + module);
				ModuleDao.instance.updateModule(planId, module);
				return Response.ok(new ResponseMessage("Changed module with id: " + module.getId(), "ok")).build();
			}

		} catch (SQLException e) { 
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();
		}
	}

	@GET
	@Path("/moduledetails/{moduleid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Module getModuleDetailsFor(@PathParam("moduleid") int moduleId) {
		try {
			return ModuleDao.instance.getModuleDetails(moduleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Module();
	}

	@DELETE
	@Path("/deletemodule/moduleid/{moduleid}")
	public Response deletePlan(@PathParam("moduleid") int moduleId) throws IOException {

		try {
			System.out.println("Delete module: " + moduleId);
			boolean resp = ModuleDao.instance.deleteModule(moduleId);
			return Response.ok(new ResponseMessage("Deleted module with id: " + moduleId + ": " + resp, "ok")).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_MODIFIED).build();

		}
	}

}