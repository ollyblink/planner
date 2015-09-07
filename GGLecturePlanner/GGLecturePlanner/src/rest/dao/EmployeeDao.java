package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Employee;

public enum EmployeeDao {
	instance;

	private PreparedStatement addEmployeeStatement;



	private EmployeeDao() {
	try {
			this.addEmployeeStatement = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"insert into employees(employee_nr, first_name, last_name, email, internal_cost_center, external_institute, is_external, is_external_paid_separately, username, password, comments) values (?,?,?,?,?,?,?,?,?,?,?);");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds a new employee
	 * 
	 * @param employee
	 * @return a message of success or fail
	 */
	public boolean addEmployee(Employee employee) {
		if (employee.getEmail() == null) {
			return false;
		} else {
			// TODO: Throw away password and put there some hash instead...
			try {
				System.out.println("ServceDao:addEmployee: employeeNr: " + employee.getEmployeeNr());
				this.addEmployeeStatement.setString(1, employee.getEmployeeNr());
				this.addEmployeeStatement.setString(2, employee.getFirstName());
				this.addEmployeeStatement.setString(3, employee.getLastName());
				this.addEmployeeStatement.setString(4, employee.getEmail());
				this.addEmployeeStatement.setInt(5, employee.getInternalCostCenter());
				this.addEmployeeStatement.setString(6, employee.getExternalInstitute());
				this.addEmployeeStatement.setBoolean(7, employee.isExternal());
				this.addEmployeeStatement.setBoolean(8, employee.isExternalPaidSeparately());
				this.addEmployeeStatement.setString(9, employee.getUsername());
				this.addEmployeeStatement.setString(10, employee.getPassword());
				this.addEmployeeStatement.setString(11, employee.getComments());

				int success = this.addEmployeeStatement.executeUpdate();

				if (success > 0) {
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> employees = new ArrayList<>();
		try {
			Statement s =  DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r = s.executeQuery("select * from employees");

			while (r.next()) {
				Employee employee = new Employee();
				employee.setEmployeeNr(r.getString(1));
				employee.setFirstName(r.getString(2));
				employee.setLastName(r.getString(3));
				employee.setEmail(r.getString(4));
				employee.setInternalCostCenter(r.getInt(5));
				employee.setExternalInstitute(r.getString(6));
				employee.setExternal(r.getBoolean(7));
				employee.setExternalPaidSeparately(r.getBoolean(8));
				employee.setUsername(r.getString(9));
				employee.setPassword(r.getString(10));
				employee.setComments(r.getString(11));
				employees.add(employee);
			}
			r.close();
			s.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}
 
}
