package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Module {
	private String primaryNr;
	private ArrayList<String> secondaryNrs;
	private String semesterNr;
	private AssessmentType assessmentType;
	private String assessmentDate;
	private Employee responsibleEmployee;
	private String comments;
	private ArrayList<Course> courses;
	private String moduleType;
	private ArrayList<String> disciplines;
	private Department department;
	
 

	public Module(String primaryNr, ArrayList<String> secondaryNrs, String semesterNr, AssessmentType assessmentType, String assessmentDate,
			Employee responsibleEmployee, String comments, ArrayList<Course> courses, String moduleType, ArrayList<String> disciplines,
			Department department) {
		this.primaryNr = primaryNr;
		this.secondaryNrs = secondaryNrs;
		this.semesterNr = semesterNr;
		this.assessmentType = assessmentType;
		this.assessmentDate = assessmentDate;
		this.responsibleEmployee = responsibleEmployee;
		this.comments = comments;
		this.courses = courses;
		this.moduleType = moduleType;
		this.disciplines = disciplines;
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public ArrayList<String> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(ArrayList<String> disciplines) {
		this.disciplines = disciplines;
	}

	public ArrayList<String> getSecondaryNrs() {
		return secondaryNrs;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public void setSecondaryNrs(ArrayList<String> secondaryNrs) {
		this.secondaryNrs = secondaryNrs;
	}

	public Module() {
	}

	public String getPrimaryNr() {
		return primaryNr;
	}

	public void setPrimaryNr(String primaryNr) {
		this.primaryNr = primaryNr;
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
}
