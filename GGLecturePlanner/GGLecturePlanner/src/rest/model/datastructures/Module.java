package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Module {

	private Integer id;
	private String semesterNr;
	private AssessmentType assessmentType;
	private String assessmentDate;
	private Employee responsibleEmployee;
	private String comments;

	private ArrayList<String> primaryNrs;  
	private Department department; 
	private ArrayList<Discipline> disciplines;
	private ArrayList<Course> courses;

	public Module() {
	}
 


	 


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
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
		return "Module [id=" + id + ", semesterNr=" + semesterNr + ", assessmentType=" + assessmentType + ", assessmentDate=" + assessmentDate
				+ ", responsibleEmployee=" + responsibleEmployee + ", comments=" + comments + ", primaryNrs=" + primaryNrs + ", department="
				+ department + ", disciplines=" + disciplines + ", courses=" + courses + "]";
	}


 

}
