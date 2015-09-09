package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Plan {
	private Integer id;
	private String semester;
	private Integer year;
	private ArrayList<Module> modules;

	public Plan() {
	}

	public Plan(Integer id, String semester, Integer year, ArrayList<Module> modules) {
		this.id = id;
		this.semester = semester;
		this.year = year;
		this.modules = modules;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public ArrayList<Module> getModules() {
		return modules;
	}

	public void setModules(ArrayList<Module> modules) {
		this.modules = modules;
	}

	@Override
	public String toString() {
		return "Plan [id=" + id + ", semester=" + semester + ", year=" + year + ", modules=" + modules + "]";
	}

 
  

}
