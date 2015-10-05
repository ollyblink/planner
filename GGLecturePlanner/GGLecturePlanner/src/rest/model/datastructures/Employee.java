package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {
	private Integer id;
	private String employeeNr;
	private String firstName;
	private String lastName;
	private String email;
	/** Kostenstelle, internal = internally employed. External = externally hired */
	private Integer internalCostCenter;
	/** for those paid via contracts */
	private String externalInstitute;
	/** x that goes into the other column, for people that are e.g. from another company */
	TrueFalseTupel isPaidSeparately;
	/** for login if needed */
	private String username;
	/** for login if needed */
	private String password;
	/** Some additional comments e.g. about the employment conditions or so */
	private String comments;

	private ArrayList<Role> roles;
	private ArrayList<Module> modulesAsLecturer;
	private ArrayList<Module> modulesAsMV;

	public Employee() {

	}
 
	public TrueFalseTupel getIsPaidSeparately() {
		return isPaidSeparately;
	}

	public void setIsPaidSeparately(TrueFalseTupel isPaidSeparately) {
		this.isPaidSeparately = isPaidSeparately;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getInternalCostCenter() {
		return internalCostCenter;
	}

	public void setInternalCostCenter(Integer internalCostCenter) {
		this.internalCostCenter = internalCostCenter;
	}

	public String getExternalInstitute() {
		return externalInstitute;
	}

	public void setExternalInstitute(String externalInstitute) {
		this.externalInstitute = externalInstitute;
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

	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}

	public ArrayList<Module> getModulesAsLecturer() {
		return modulesAsLecturer;
	}

	public void setModulesAsLecturer(ArrayList<Module> modulesAsLecturer) {
		this.modulesAsLecturer = modulesAsLecturer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

 

	@Override
	public String toString() {
		return "Employee [id=" + id + ", employeeNr=" + employeeNr + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", internalCostCenter=" + internalCostCenter + ", externalInstitute=" + externalInstitute + ", isPaidSeparately="
				+ isPaidSeparately + ", username=" + username + ", password=" + password + ", comments=" + comments + ", roles=" + roles
				+ ", modulesAsLecturer=" + modulesAsLecturer + ", modulesAsMV=" + modulesAsMV + "]";
	}

	public void setModulesAsMV(ArrayList<Module> modulesAsMV) {
		this.modulesAsMV = modulesAsMV;
	}

	public ArrayList<Module> getModulesAsMV() {
		return modulesAsMV;
	}
 

}
