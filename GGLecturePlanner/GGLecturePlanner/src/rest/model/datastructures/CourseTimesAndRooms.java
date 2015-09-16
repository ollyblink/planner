package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseTimesAndRooms {

	private int id;
	private int courseId;
	private int moduleId;
	private Times times;
	private String dayOfWeek;
	private int roomCapacity;
	private String roomLabel;
	private String comments;

	public CourseTimesAndRooms() {
	}

	public CourseTimesAndRooms(int id, int courseId, int moduleId, Times times, String dayOfWeek, int roomCapacity, String roomLabel, String comments) {
		this.id = id;
		this.courseId = courseId;
		this.moduleId = moduleId;
		this.times = times;
		this.dayOfWeek = dayOfWeek;
		this.roomCapacity = roomCapacity;
		this.roomLabel = roomLabel;
		this.comments = comments;
	}

	public void setId(int id) {
		this.id = id;

	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public void setTimes(Times times) {
		this.times = times;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getId() {
		return id;
	}

	public int getCourseId() {
		return courseId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public Times getTimes() {
		return times;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public String getRoomLabel() {
		return roomLabel;
	}

	public String getComments() {
		return comments;
	}

	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public void setRoomLabel(String roomLabel) {
		this.roomLabel = roomLabel;

	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "CourseTimesAndRooms [id=" + id + ", courseId=" + courseId + ", moduleId=" + moduleId + ", times=" + times + ", dayOfWeek="
				+ dayOfWeek + ", roomCapacity=" + roomCapacity + ", roomLabel=" + roomLabel + ", comments=" + comments + "]";
	}

}
