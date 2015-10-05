package rest.model.datastructures;

import java.sql.Date;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {
	private Integer id;
	private String courseDescription;
	private Integer moduleNr;
	private String vvzNr;
	private Integer nrOfGroups;
	private Integer nrOfStudentsExpectedPerGroup;
	private Boolean isMaxNrStudentsExpectedPerGroup;
	private Float swsTotalPerGroup;
	private String date;
	private Date startDate;
	private Date endDate;
	private String rythm;
	private String comments;
	private CourseType courseType;
	private ArrayList<String> moduleParts;
	private ArrayList<CourseTimesAndRooms> courseTimesAndRooms;
	private ArrayList<Employee> lecturers;

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseDescription=" + courseDescription + ", moduleNr=" + moduleNr + ", vvzNr=" + vvzNr + ", nrOfGroups="
				+ nrOfGroups + ", nrOfStudentsExpectedPerGroup=" + nrOfStudentsExpectedPerGroup + ", isMaxNrStudentsExpectedPerGroup="
				+ isMaxNrStudentsExpectedPerGroup + ", swsTotalPerGroup=" + swsTotalPerGroup + ", date=" + date + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", rythm=" + rythm + ", comments=" + comments + ", courseType=" + courseType + ", moduleParts="
				+ moduleParts + ", courseTimesAndRooms=" + courseTimesAndRooms + ", lecturers=" + lecturers + "]";
	}

	public Course() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getNrOfGroups() {
		return nrOfGroups;
	}

	public void setNrOfGroups(Integer nrOfGroups) {
		this.nrOfGroups = nrOfGroups;
	}

	public Integer getNrOfStudentsExpectedPerGroup() {
		return nrOfStudentsExpectedPerGroup;
	}

	public void setNrOfStudentsExpectedPerGroup(Integer nrOfStudentsExpectedPerGroup) {
		this.nrOfStudentsExpectedPerGroup = nrOfStudentsExpectedPerGroup;
	}

	public Boolean getIsMaxNrStudentsExpectedPerGroup() {
		return isMaxNrStudentsExpectedPerGroup;
	}

	public void setIsMaxNrStudentsExpectedPerGroup(Boolean isMaxNrStudentsExpectedPerGroup) {
		this.isMaxNrStudentsExpectedPerGroup = isMaxNrStudentsExpectedPerGroup;
	}

	public Float getSwsTotalPerGroup() {
		return swsTotalPerGroup;
	}

	public void setSwsTotalPerGroup(Float swsTotalPerGroup) {
		this.swsTotalPerGroup = swsTotalPerGroup;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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

	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public ArrayList<String> getModuleParts() {
		return moduleParts;
	}

	public void setModuleParts(ArrayList<String> moduleParts) {
		this.moduleParts = moduleParts;
	}

	public ArrayList<CourseTimesAndRooms> getCourseTimesAndRooms() {
		return courseTimesAndRooms;
	}

	public void setCourseTimesAndRooms(ArrayList<CourseTimesAndRooms> courseTimesAndRooms) {
		this.courseTimesAndRooms = courseTimesAndRooms;
	}

	public ArrayList<Employee> getLecturers() {
		return lecturers;
	}

	public void setLecturers(ArrayList<Employee> lecturers) {
		this.lecturers = lecturers;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

}
