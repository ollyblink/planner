package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {
	private Integer id;
	private String courseDescription;
	private Integer moduleNr;
	private String vvzNr;
	private String modulePart;
	private Integer nrOfGroups;
	private Integer nrOfStudentsExpectedPerGroup;
	private Boolean isMaxNrStudentsExpectedPerGroup;
	private Float swsTotalPerGroup; 
	private String startDate;
	private String endDate;
	private String rythm;
	private String comments;
	private CourseType courseType;

	private ArrayList<CourseTimesAndRooms> courseTimesAndRooms;
	private ArrayList<Employee> lecturers;
	
	public Course() {
	}

	
 

	public Course(Integer id, String courseDescription, Integer moduleNr, String vvzNr, String modulePart, Integer nrOfGroups,
			Integer nrOfStudentsExpectedPerGroup, Boolean isMaxNrStudentsExpectedPerGroup, Float swsTotalPerGroup, String startDate, String endDate,
			String rythm, String comments, CourseType courseType, ArrayList<CourseTimesAndRooms> courseTimesAndRooms, ArrayList<Employee> lecturers) {
		this.id = id;
		this.courseDescription = courseDescription;
		this.moduleNr = moduleNr;
		this.vvzNr = vvzNr;
		this.modulePart = modulePart;
		this.nrOfGroups = nrOfGroups;
		this.nrOfStudentsExpectedPerGroup = nrOfStudentsExpectedPerGroup;
		this.isMaxNrStudentsExpectedPerGroup = isMaxNrStudentsExpectedPerGroup;
		this.swsTotalPerGroup = swsTotalPerGroup;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rythm = rythm;
		this.comments = comments;
		this.courseType = courseType;
		this.courseTimesAndRooms = courseTimesAndRooms;
		this.lecturers = lecturers;
	}




	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Boolean getIsMaxNrStudentsExpectedPerGroup() {
		return isMaxNrStudentsExpectedPerGroup;
	}


	public void setIsMaxNrStudentsExpectedPerGroup(Boolean isMaxNrStudentsExpectedPerGroup) {
		this.isMaxNrStudentsExpectedPerGroup = isMaxNrStudentsExpectedPerGroup;
	}


	public void setNrOfGroups(Integer nrOfGroups) {
		this.nrOfGroups = nrOfGroups;
	}


	public void setNrOfStudentsExpectedPerGroup(Integer nrOfStudentsExpectedperGroup) {
		this.nrOfStudentsExpectedPerGroup = nrOfStudentsExpectedperGroup;
	}


	public void setSwsTotalPerGroup(Float swsTotalPerGroup) {
		this.swsTotalPerGroup = swsTotalPerGroup;
	}


 


	public ArrayList<Employee> getLecturers() {
		return lecturers;
	}

	public void setLecturers(ArrayList<Employee> lecturers) {
		this.lecturers = lecturers;
	}

	public ArrayList<CourseTimesAndRooms> getCourseTimesAndRooms() {
		return courseTimesAndRooms;
	}

	public void setCourseTimesAndRooms(ArrayList<CourseTimesAndRooms> courseTimesAndRooms) {
		this.courseTimesAndRooms = courseTimesAndRooms;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public Integer getModuleNr() {
		return moduleNr;
	}

	public void setModuleNr(Integer moduleNr) {
		this.moduleNr = moduleNr;
	}

	public String getVvzNr() {
		return vvzNr;
	}

	public void setVvzNr(String vvzNr) {
		this.vvzNr = vvzNr;
	}

	public String getModulePart() {
		return modulePart;
	}

	public void setModulePart(String modulePart) {
		this.modulePart = modulePart;
	}

	public int getNrOfGroups() {
		return nrOfGroups;
	}

	public void setNrOfGroups(int nrOfGroups) {
		this.nrOfGroups = nrOfGroups;
	}

	public int getNrOfStudentsExpectedperGroup() {
		return nrOfStudentsExpectedPerGroup;
	}

	public void setNrOfStudentsExpectedPerGroup(int nrOfStudentsExpectedperGroup) {
		this.nrOfStudentsExpectedPerGroup = nrOfStudentsExpectedperGroup;
	}

	public boolean isMaxNrStudentsExpectedPerGroup() {
		return isMaxNrStudentsExpectedPerGroup;
	}

	public void setMaxNrStudentsExpectedPerGroup(boolean isMaxNrStudentsExpectedPerGroup) {
		this.isMaxNrStudentsExpectedPerGroup = isMaxNrStudentsExpectedPerGroup;
	}

	public float getSwsTotalPerGroup() {
		return swsTotalPerGroup;
	}

	public void setSwsTotalPerGroup(float swsTotalPerGroup) {
		this.swsTotalPerGroup = swsTotalPerGroup;
	}

 

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRythm() {
		return rythm;
	}

	public void setRythm(String rythm) {
		this.rythm = rythm;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

 


	@Override
	public String toString() {
		return "Course [id=" + id + ", courseDescription=" + courseDescription + ", moduleNr=" + moduleNr + ", vvzNr=" + vvzNr + ", modulePart="
				+ modulePart + ", nrOfGroups=" + nrOfGroups + ", nrOfStudentsExpectedPerGroup=" + nrOfStudentsExpectedPerGroup
				+ ", isMaxNrStudentsExpectedPerGroup=" + isMaxNrStudentsExpectedPerGroup + ", swsTotalPerGroup=" + swsTotalPerGroup + ", startDate="
				+ startDate + ", endDate=" + endDate + ", rythm=" + rythm + ", comments=" + comments + ", courseType=" + courseType
				+ ", courseTimesAndRooms=" + courseTimesAndRooms + ", lecturers=" + lecturers + "]";
	}




	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}


	public CourseType getCourseType() {
		return courseType;
	}

	 

}
