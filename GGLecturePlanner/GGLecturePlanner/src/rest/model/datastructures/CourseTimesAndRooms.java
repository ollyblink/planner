package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseTimesAndRooms {

	private Integer id;
	private Integer courseId;
	private Integer moduleId;
	private Times times;
	private String dayOfWeek;
	private Integer roomCapacity;
	private String roomLabel;
	private String comments;

	public CourseTimesAndRooms() {
	}

	public CourseTimesAndRooms(Integer id, Integer courseId, Integer moduleId, Times times, String dayOfWeek, Integer roomCapacity, String roomLabel,
			String comments) {
		this.id = id;
		this.courseId = courseId;
		this.moduleId = moduleId;
		this.times = times;
		this.dayOfWeek = dayOfWeek;
		this.roomCapacity = roomCapacity;
		this.roomLabel = roomLabel;
		this.comments = comments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Times getTimes() {
		return times;
	}

	public void setTimes(Times times) {
		this.times = times;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(Integer roomCapacity) {
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

	@Override
	public String toString() {
		return "CourseTimesAndRooms [id=" + id + ", courseId=" + courseId + ", moduleId=" + moduleId + ", times=" + times + ", dayOfWeek="
				+ dayOfWeek + ", roomCapacity=" + roomCapacity + ", roomLabel=" + roomLabel + ", comments=" + comments + "]";
	}

}
