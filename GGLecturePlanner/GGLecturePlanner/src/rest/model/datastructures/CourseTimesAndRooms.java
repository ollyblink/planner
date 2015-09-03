package rest.model.datastructures;

import java.sql.Time;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseTimesAndRooms {
	private String courseDescription;
	private Time startTime;
	private Time endTime;
	private String dayAbbreviation;
	private int roomCapacity;
	private String roomLabel;
	private String comments;

	public CourseTimesAndRooms() {
	}

	public CourseTimesAndRooms(String courseDescription, Time startTime, Time endTime, String dayAbbreviation, int roomCapacity, String roomLabel,
			String comments) {
		this.courseDescription = courseDescription;
		this.startTime = startTime;
		this.endTime = endTime;
		this.dayAbbreviation = dayAbbreviation;
		this.roomCapacity = roomCapacity;
		this.roomLabel = roomLabel;
		this.comments = comments;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getDayAbbreviation() {
		return dayAbbreviation;
	}

	public void setDayAbbreviation(String dayAbbreviation) {
		this.dayAbbreviation = dayAbbreviation;
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public String getRoomLabel() {
		return roomLabel;
	}

	public void setRoomLabel(String roomLabel) {
		this.roomLabel = roomLabel;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
