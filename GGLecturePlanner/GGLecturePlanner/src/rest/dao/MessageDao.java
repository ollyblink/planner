package rest.dao;

import rest.model.datastructures.Employee;

public enum MessageDao {
	instance;

	public void sendMessage(Employee employee, String pw) {
		// TODO Auto-generated method stub
		System.out.println("Password: " + pw);
	 
	}
	
}
