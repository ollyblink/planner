package rest.model.datastructures;

import java.sql.Time;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Times {
	private Time startTime;
	private Time endTime;

	public Times() {
	}

	public Times(Time startTime, Time endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
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
 
}
