package rest.model.resources;

import java.util.ArrayList;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.dao.StaticDataDao;
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
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<AssessmentType> getAllAssessmentTypes() {
		return new ArrayList<>(StaticDataDao.instance.getAssessmentTypes().values());
	}

	@GET
	@Path("/coursetypes")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<CourseType> getAllCourseTypes() {
		return new ArrayList<>(StaticDataDao.instance.getCourseTypes().values());
	}

	@GET
	@Path("/moduletypes")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<ModuleType> getAllModuleTypes() {
		return new ArrayList<>(StaticDataDao.instance.getModuleTypes().values());
	}

	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Role> getAllRoles() {
		return new ArrayList<>(StaticDataDao.instance.getRoles().values());
	}

	@GET
	@Path("/disciplines")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Discipline> getAllDisciplines() {
		return new ArrayList<>(StaticDataDao.instance.getDisciplines().values());
	}

	@GET
	@Path("/departments")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Department> getAllDepartments() {
		return new ArrayList<>(StaticDataDao.instance.getDepartments().values());
	}

	@GET
	@Path("/semestertypes")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<SemesterType> getAllSemesterTypes() {
		return StaticDataDao.instance.getSemesterTypes();
	}

	@GET
	@Path("/years")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Integer> getAllYears() {
		return StaticDataDao.instance.getYears();
	}

	@GET
	@Path("/numbers")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public ArrayList<Integer> getNumberRange() {
		return StaticDataDao.instance.getNumberRange(1, 10);
	}
	@GET
	@Path("/dayabbreviations")
	@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
	public Set<String> getDayAbbreviations() {
		return StaticDataDao.instance.getDayAbbreviations();
	}
}
