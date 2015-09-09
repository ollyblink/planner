package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Module {

	private Integer id;
	private ArrayList<String> primaryNrs; 
	private String semesterNr;
	private AssessmentType assessmentType;
	private String assessmentDate;
	private Employee responsibleEmployee;
	private String comments;
	private ArrayList<Course> courses;
	private ArrayList<ModuleType> moduleTypes;
	private ArrayList<Discipline> disciplines;
	private Department department;

	public Module() {
	}
 


	public Module(Integer id, ArrayList<String> primaryNrs, String semesterNr, AssessmentType assessmentType, String assessmentDate,
			Employee responsibleEmployee, String comments, ArrayList<Course> courses, ArrayList<ModuleType> moduleTypes,
			ArrayList<Discipline> disciplines, Department department) {
		this.id = id;
		this.primaryNrs = primaryNrs;
		this.semesterNr = semesterNr;
		this.assessmentType = assessmentType;
		this.assessmentDate = assessmentDate;
		this.responsibleEmployee = responsibleEmployee;
		this.comments = comments;
		this.courses = courses;
		this.moduleTypes = moduleTypes;
		this.disciplines = disciplines;
		this.department = department;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public ArrayList<ModuleType> getModuleTypes() {
		return moduleTypes;
	}



	public void setModuleTypes(ArrayList<ModuleType> moduleTypes) {
		this.moduleTypes = moduleTypes;
	}



	public ArrayList<Discipline> getDisciplines() {
		return disciplines;
	}



	public void setDisciplines(ArrayList<Discipline> disciplines) {
		this.disciplines = disciplines;
	}


 



	public ArrayList<String> getPrimaryNrs() {
		return primaryNrs;
	}


	public void setPrimaryNrs(ArrayList<String> primaryNrs) {
		this.primaryNrs = primaryNrs;
	}


	 

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

 
 
	public String getSemesterNr() {
		return semesterNr;
	}

	public void setSemesterNr(String semesterNr) {
		this.semesterNr = semesterNr;
	}

	public AssessmentType getAssessmentType() {
		return assessmentType;
	}

	public void setAssessmentType(AssessmentType assessmentType) {
		this.assessmentType = assessmentType;
	}

	public String getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(String assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

 

	public Employee getResponsibleEmployee() {
		return responsibleEmployee;
	}


	public void setResponsibleEmployee(Employee responsibleEmployee) {
		this.responsibleEmployee = responsibleEmployee;
	}


	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}



	@Override
	public String toString() {
		return "Module [id=" + id + ", primaryNrs=" + primaryNrs + ", semesterNr=" + semesterNr + ", assessmentType=" + assessmentType
				+ ", assessmentDate=" + assessmentDate + ", responsibleEmployee=" + responsibleEmployee + ", comments=" + comments + ", courses="
				+ courses + ", moduleTypes=" + moduleTypes + ", disciplines=" + disciplines + ", department=" + department + "]";
	}

}
