package rest.model.resources;

import java.util.ArrayList;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.dao.StaticTypesDao;
import rest.model.datastructures.AssessmentType;
import rest.model.datastructures.CourseType;
import rest.model.datastructures.Department;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.ModuleType;
import rest.model.datastructures.Role;
import rest.model.datastructures.SemesterType;

@Path("/staticresources")
public class StaticDataResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Path("/assessmenttypes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<AssessmentType> getAllAssessmentTypes() {
		return new ArrayList<>(StaticTypesDao.instance.getAssessmentTypes().values());
	}

	@GET
	@Path("/coursetypes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<CourseType> getAllCourseTypes() {
		return new ArrayList<>(StaticTypesDao.instance.getCourseTypes().values());
	}

	@GET
	@Path("/moduletypes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ModuleType> getAllModuleTypes() {
		return new ArrayList<>(StaticTypesDao.instance.getModuleTypes().values());
	}

	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Role> getAllRoles() {
		return new ArrayList<>(StaticTypesDao.instance.getRoles().values());
	}

	@GET
	@Path("/disciplines")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Discipline> getAllDisciplines() {
		return new ArrayList<>(StaticTypesDao.instance.getDisciplines().values());
	}

	@GET
	@Path("/departments")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Department> getAllDepartments() {
		return new ArrayList<>(StaticTypesDao.instance.getDepartments().values());
	}

	@GET
	@Path("/semestertypes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SemesterType> getAllSemesterTypes() {
		return StaticTypesDao.instance.getSemesterTypes();
	}

	@GET
	@Path("/years")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> getAllYears() {
		return StaticTypesDao.instance.getYears();
	}

	@GET
	@Path("/numbers")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> getNumberRange() {
		return StaticTypesDao.instance.getNumberRange(1, 10);
	}

}
