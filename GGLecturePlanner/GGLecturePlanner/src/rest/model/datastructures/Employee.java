package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {
	private String employeeNr;
	private String firstName;
	private String lastName;
	private String email;
	/** Kostenstelle, internal = internally employed. External = externally hired */
	private Integer internalCostCenter;
	/** for those paid via contracts */
	private String externalInstitute;
	/** check if internal or external */
	private Boolean isExternal;
	/** x that goes into the other column, for people that are e.g. from another company */
	private Boolean isExternalPaidSeparately;
	/** for login if needed */
	private String username;
	/** for login if needed */
	private String password;
	/** Some additional comments e.g. about the employment conditions or so */
	private String comments;

	private ArrayList<Role> roles;
	private ArrayList<Course> courses;

	public Employee() {

	}

  


 


	public Employee(String employeeNr, String firstName, String lastName, String email, Integer internalCostCenter, String externalInstitute,
			Boolean isExternal, Boolean isExternalPaidSeparately, String username, String password, String comments, ArrayList<Role> roles,
			ArrayList<Course> courses) {
		this.employeeNr = employeeNr;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.internalCostCenter = internalCostCenter;
		this.externalInstitute = externalInstitute;
		this.isExternal = isExternal;
		this.isExternalPaidSeparately = isExternalPaidSeparately;
		this.username = username;
		this.password = password;
		this.comments = comments;
		this.roles = roles;
		this.courses = courses;
	}







	public Boolean getIsExternal() {
		return isExternal;
	}







	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}







	public Boolean getIsExternalPaidSeparately() {
		return isExternalPaidSeparately;
	}







	public void setIsExternalPaidSeparately(Boolean isExternalPaidSeparately) {
		this.isExternalPaidSeparately = isExternalPaidSeparately;
	}







	public void setInternalCostCenter(Integer internalCostCenter) {
		this.internalCostCenter = internalCostCenter;
	}







	public ArrayList<Course> getCourses() {
		return courses;
	}


	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}


	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}

	public String getEmployeeNr() {
		return employeeNr;
	}

	public void setEmployeeNr(String employeeNr) {
		this.employeeNr = employeeNr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getInternalCostCenter() {
		return internalCostCenter;
	}

	public void setInternalCostCenter(int internalCostCenter) {
		this.internalCostCenter = internalCostCenter;
	}

	public String getExternalInstitute() {
		return externalInstitute;
	}

	public void setExternalInstitute(String externalInstitute) {
		this.externalInstitute = externalInstitute;
	}

	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	public boolean isExternalPaidSeparately() {
		return isExternalPaidSeparately;
	}

	public void setExternalPaidSeparately(boolean isExternalPaidSeparately) {
		this.isExternalPaidSeparately = isExternalPaidSeparately;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
