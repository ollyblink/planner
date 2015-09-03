package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Plan {
	private String semester;
	private int year;

	public Plan() {
	}

	public Plan(String semester, int year) {
		this.semester = semester;
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
