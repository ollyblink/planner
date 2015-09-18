package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Department implements Comparable<Department> {
	private String deptName;
	private String fieldOfExpertise;
	private Integer id;

	public Department() {
	}

	public Department(Integer id, String deptName, String fieldOfExpertise) {
		this.id = id;
		this.deptName = deptName;
		this.fieldOfExpertise = fieldOfExpertise;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getFieldOfExpertise() {
		return fieldOfExpertise;
	}

	public void setFieldOfExpertise(String fieldOfExpertise) {
		this.fieldOfExpertise = fieldOfExpertise;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

	@Override
	public String toString() {
		return "Department [deptName=" + deptName + ", fieldOfExpertise=" + fieldOfExpertise + ", id=" + id + "]";
	}

	@Override
	public int compareTo(Department o) {
		return o.deptName.compareTo(deptName);
	}

}
