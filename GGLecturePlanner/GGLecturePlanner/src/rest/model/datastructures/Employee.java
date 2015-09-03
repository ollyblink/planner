package rest.model.datastructures;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {
	private String employeeNr;
	private String firstName;
	private String lastName;
	private String email;
	/** for login if needed */
	private String username;
	/** for login if needed */
	private String password;
	/** Some additional comments e.g. about the employment conditions or so */
	private String comments;

	private ArrayList<Role> roles;

	public Employee() {

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
