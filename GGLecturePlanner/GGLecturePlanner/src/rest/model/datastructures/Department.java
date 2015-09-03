package rest.model.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Department {
	private String deptName;
	private String fieldOfExpertise;

	public Department() {
	}

	public Department(String deptName, String fieldOfExpertise) {
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

}
