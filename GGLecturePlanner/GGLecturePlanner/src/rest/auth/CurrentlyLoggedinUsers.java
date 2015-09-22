package rest.auth;

import java.util.HashSet;
import java.util.Set;

import rest.model.datastructures.Employee;

public enum CurrentlyLoggedinUsers {
	instance;

	private Set<Employee> loggedInUsers;

	private CurrentlyLoggedinUsers() {
		this.loggedInUsers = new HashSet<Employee>();
	}

	public void addLoggedInUser(Employee userForUsername) {
		this.loggedInUsers.add(userForUsername);
	}

	public void removeLoggedInUser(Employee userForUsername) {
		this.loggedInUsers.remove(userForUsername);
	}

	public boolean containsUser(Employee user) {
		return loggedInUsers.contains(user);
	}
}
