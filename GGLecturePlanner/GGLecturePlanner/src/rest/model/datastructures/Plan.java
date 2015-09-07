package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Plan {
	private String semester;
	private int year;
	private ArrayList<SimplePlanModule> modules;

	public Plan() {
	}

 

	public Plan(String semester, int year, ArrayList<SimplePlanModule> modules) {
		this.semester = semester;
		this.year = year;
		this.modules = modules;
	}



	public ArrayList<SimplePlanModule> getModules() {
		return modules;
	}



	public void setModules(ArrayList<SimplePlanModule> modules) {
		this.modules = modules;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((semester == null) ? 0 : semester.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plan other = (Plan) obj;
		if (semester == null) {
			if (other.semester != null)
				return false;
		} else if (!semester.equals(other.semester))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Plan [semester=" + semester + ", year=" + year + ", modules=" + modules + "]";
	}
	
	

}
