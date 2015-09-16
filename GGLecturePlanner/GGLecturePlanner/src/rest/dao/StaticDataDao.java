package rest.dao;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rest.model.datastructures.AssessmentType;
import rest.model.datastructures.CourseType;
import rest.model.datastructures.Department;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.ModuleType;
import rest.model.datastructures.Role;
import rest.model.datastructures.SemesterType;

public enum StaticDataDao {
	//

	instance;

	/** Used for splitting transferred data */
	public final String outerSplittingPattern = ";;;";
	public final String innerSplittingPattern = "%%%";
	
	private Map<String, AssessmentType> assessmentTypes;
	private Map<String, Role> roles;
	private Map<String, CourseType> courseTypes;
	private Map<String, ModuleType> moduleTypes;
	private Map<String, Discipline> disciplines;
	private Map<String, Department> departments;
	private Set<String> dayAbbreviations;

	private StaticDataDao() {

		this.assessmentTypes = getAllAssessmentTypes();
		this.roles = getAllRoles();
		this.courseTypes = getAllCourseTypes();
		this.moduleTypes = getAllModuleType();
		this.disciplines = getAllDisciplines();
		this.departments = getAllDepartments();
		this.dayAbbreviations = getAllDayAbbreviations();
	}

	private Set<String> getAllDayAbbreviations() {
		Set<String> dayAbbreviations = new HashSet<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from day_abbr;");
			while (res.next()) {
				  
					dayAbbreviations.add(res.getString(1)); 
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return dayAbbreviations;
	}

	private Map<String, Department> getAllDepartments() {
		Map<String, Department> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from departments;");
			while (res.next()) {
				data.put(res.getString("dept_name"),
						new Department(res.getInt("id"), res.getString("dept_name"), res.getString("field_of_expertise")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	private Map<String, Discipline> getAllDisciplines() {
		Map<String, Discipline> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from disciplines;");
			while (res.next()) {
				data.put(res.getString("abbr"), new Discipline(res.getString("abbr"), res.getString("description")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	private Map<String, ModuleType> getAllModuleType() {
		Map<String, ModuleType> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from module_types;");
			while (res.next()) {
				data.put(res.getString("abbr"), new ModuleType(res.getString("abbr"), res.getString("description")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	private Map<String, CourseType> getAllCourseTypes() {
		Map<String, CourseType> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from course_types;");
			while (res.next()) {
				data.put(res.getString("abbr"), new CourseType(res.getString("abbr"), res.getString("description")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	private Map<String, Role> getAllRoles() {
		Map<String, Role> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from roles;");
			while (res.next()) {
				data.put(res.getString("abbr"), new Role(res.getString("abbr"), res.getString("description")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	private Map<String, AssessmentType> getAllAssessmentTypes() {
		Map<String, AssessmentType> data = new TreeMap<>();
		try {
			Statement stat = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet res = stat.executeQuery("Select * from assessment_types;");
			while (res.next()) {
				data.put(res.getString("abbr"), new AssessmentType(res.getString("abbr"), res.getString("description")));
			}
			res.close();
			stat.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return data;
	}

	public ArrayList<Integer> getYears() {
		ArrayList<Integer> years = new ArrayList<>();
		for (int year = 1970; year < 2050; ++year) {
			years.add(year);
		}
		return years;
	}

	public ArrayList<SemesterType> getSemesterTypes() {
		ArrayList<SemesterType> semesterTypes = new ArrayList<>();

		semesterTypes.add(new SemesterType("FS", "Frühlingssemester"));
		// semesterTypes.add(new SemesterType("SS", "Sommersemester"));
		semesterTypes.add(new SemesterType("HS", "Herbstsemester"));
		// semesterTypes.add(new SemesterType("WS", "Wintersemester"));

		return semesterTypes;
	}

	public AssessmentType getAssessmentType(String abbreviation) {
		return this.assessmentTypes.get(abbreviation);
	}

	public ModuleType getModuleType(String abbreviation) {
		return this.moduleTypes.get(abbreviation);
	}

	public Role getRole(String abbreviation) {
		return this.roles.get(abbreviation);
	}

	public Discipline getDiscipline(String abbreviation) {
		return this.disciplines.get(abbreviation);
	}

	public CourseType getCourseType(String abbreviation) {
		return this.courseTypes.get(abbreviation);
	}

	public Department getDepartment(String deptName) {
		return this.departments.get(deptName);
	}

	public Map<String, AssessmentType> getAssessmentTypes() {
		return assessmentTypes;
	}

	public Map<String, Role> getRoles() {
		return roles;
	}

	public Map<String, CourseType> getCourseTypes() {
		return courseTypes;
	}

	public Map<String, ModuleType> getModuleTypes() {
		return moduleTypes;
	}

	public Map<String, Discipline> getDisciplines() {
		return disciplines;
	}

	public Map<String, Department> getDepartments() {
		return departments;
	}
	
	

	public Set<String> getDayAbbreviations() {
		return dayAbbreviations;
	}
 

	public ArrayList<Integer> getNumberRange(int min, int max) {

		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = min; i < max; ++i) {
			numbers.add(new Integer(i));
		}
		return numbers;
	}

}
