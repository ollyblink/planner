package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {
	private String courseDescription;
	private String moduleNr;
	private String vvzNr;
	private String modulePart;
	private int nrOfGroups;
	private int nrOfStudentsExpectedperGroup;
	private boolean isMaxNrStudentsExpectedPerGroup;
	private float swsTotalPerGroup;
	private float nrOfLecturers;
	private float swsPerLecturer;
	private String startDate;
	private String endDate;
	private String rythm;
	private String comments;
	private ArrayList<CourseTimesAndRooms> courseTimesAndRooms;
	private ArrayList<Lecturer> lecturers;

	public Course() {
	}

	public Course(String courseDescription, String moduleNr, String vvzNr, String modulePart, int nrOfGroups, int nrOfStudentsExpectedperGroup,
			boolean isMaxNrStudentsExpectedPerGroup, float swsTotalPerGroup, float nrOfLecturers, float swsPerLecturer, String startDate,
			String endDate, String rythm, String comments, ArrayList<CourseTimesAndRooms> courseTimesAndRooms) {
		this.courseDescription = courseDescription;
		this.moduleNr = moduleNr;
		this.vvzNr = vvzNr;
		this.modulePart = modulePart;
		this.nrOfGroups = nrOfGroups;
		this.nrOfStudentsExpectedperGroup = nrOfStudentsExpectedperGroup;
		this.isMaxNrStudentsExpectedPerGroup = isMaxNrStudentsExpectedPerGroup;
		this.swsTotalPerGroup = swsTotalPerGroup;
		this.nrOfLecturers = nrOfLecturers;
		this.swsPerLecturer = swsPerLecturer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rythm = rythm;
		this.comments = comments;
		this.courseTimesAndRooms = courseTimesAndRooms;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getModuleNr() {
		return moduleNr;
	}

	public void setModuleNr(String moduleNr) {
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
		return nrOfStudentsExpectedperGroup;
	}

	public void setNrOfStudentsExpectedperGroup(int nrOfStudentsExpectedperGroup) {
		this.nrOfStudentsExpectedperGroup = nrOfStudentsExpectedperGroup;
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

	public float getNrOfLecturers() {
		return nrOfLecturers;
	}

	public void setNrOfLecturers(float nrOfLecturers) {
		this.nrOfLecturers = nrOfLecturers;
	}

	public float getSwsPerLecturer() {
		return swsPerLecturer;
	}

	public void setSwsPerLecturer(float swsPerLecturer) {
		this.swsPerLecturer = swsPerLecturer;
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

}
