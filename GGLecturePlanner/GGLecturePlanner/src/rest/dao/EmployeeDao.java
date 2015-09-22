package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rest.auth.CurrentlyLoggedinUsers;
import rest.auth.PasswordFactory;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Role;

public enum EmployeeDao {
	instance;

	private PreparedStatement addEmployeeStatement;
	private PreparedStatement addEmployeeRolesStatement;
	private PreparedStatement getEmployeeFirstAndLastName;
	private PreparedStatement getEmployeeById;
	private PreparedStatement getRoles;
	private PreparedStatement checkUserPw;
	private PreparedStatement deleteEmployee;
	private PreparedStatement updateEmployeeStatement;
	private PreparedStatement deleteRolesFromEmployee;
	private PreparedStatement getUserForUsername;

	  
	private EmployeeDao() {
		try {
			this.addEmployeeStatement = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"insert into employees(id, employee_nr, first_name, last_name, email, internal_cost_center, external_institute, is_external_paid_separately, username, password, comments) values (?,?,?,?,?,?,?,?,?,?,?);");

			this.addEmployeeRolesStatement = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into employees_to_roles values (?,?);");

			this.getEmployeeFirstAndLastName = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select e.first_name, e.last_name from employees e where e.id= ?;");

			this.getEmployeeById = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select e.* from employees e where e.id= ?;");

			this.getRoles = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select role_fk as role from employees_to_roles where employee_fk=?;");

			this.checkUserPw = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select password from employees where username=?;");

			this.deleteEmployee = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("delete from employees where id=?");

			this.updateEmployeeStatement = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"Update employees set employee_nr=?, first_name=?, last_name=?, email=?, internal_cost_center=?, external_institute=?, is_external_paid_separately=?, username=?, comments=? where id=?;");
			this.deleteRolesFromEmployee = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("Delete from employees_to_roles where employee_fk=?;");
			
			this.getUserForUsername =  DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select id from employees where username=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds a new employee
	 * 
	 * @param employee
	 * @return a message of success or fail
	 * @throws SQLException
	 */
	public String addEmployee(Employee employee) throws SQLException {
		String password = PasswordFactory.instance.generatePassword();
		String passwordHash = PasswordFactory.instance.generatePWHash(password);

		this.addEmployeeStatement.setInt(1, employee.getId());
		this.addEmployeeStatement.setString(2, employee.getEmployeeNr());
		this.addEmployeeStatement.setString(3, employee.getFirstName());
		this.addEmployeeStatement.setString(4, employee.getLastName());
		this.addEmployeeStatement.setString(5, employee.getEmail());
		this.addEmployeeStatement.setInt(6, employee.getInternalCostCenter());
		this.addEmployeeStatement.setString(7, employee.getExternalInstitute());
		this.addEmployeeStatement.setBoolean(8, employee.getIsPaidSeparately());
		this.addEmployeeStatement.setString(9, employee.getUsername());
		this.addEmployeeStatement.setString(10, passwordHash);
		this.addEmployeeStatement.setString(11, employee.getComments());

		this.addEmployeeStatement.executeUpdate();

		addRoles(employee.getId(), employee.getRoles());

		return password;

	}

	private void addRoles(int employeeId, ArrayList<Role> roles) throws SQLException {
		for (Role role : roles) {
			this.addEmployeeRolesStatement.setInt(1, employeeId);
			this.addEmployeeRolesStatement.setString(2, role.getAbbreviation());
			this.addEmployeeRolesStatement.executeUpdate();
		}
	}

	public void updateEmployee(Employee employee) throws SQLException {

		this.updateEmployeeStatement.setString(1, employee.getEmployeeNr());
		this.updateEmployeeStatement.setString(2, employee.getFirstName());
		this.updateEmployeeStatement.setString(3, employee.getLastName());
		this.updateEmployeeStatement.setString(4, employee.getEmail());
		this.updateEmployeeStatement.setInt(5, employee.getInternalCostCenter());
		this.updateEmployeeStatement.setString(6, employee.getExternalInstitute());
		this.updateEmployeeStatement.setBoolean(7, employee.getIsPaidSeparately());
		this.updateEmployeeStatement.setString(8, employee.getUsername());
		this.updateEmployeeStatement.setString(9, employee.getComments());
		this.updateEmployeeStatement.setInt(10, employee.getId());

		this.updateEmployeeStatement.executeUpdate();
 
		deleteRolesFromEmployee(employee.getId());

		addRoles(employee.getId(), employee.getRoles());
	}

	private void deleteRolesFromEmployee(Integer employeeId) throws SQLException {
		this.deleteRolesFromEmployee.setInt(1, employeeId);
		this.deleteRolesFromEmployee.executeUpdate();
	}

	public ArrayList<Employee> getEmployees() throws SQLException {
		ArrayList<Employee> employees = new ArrayList<>();

		Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
		ResultSet r = s.executeQuery("select * from employees_to_roles");
		Map<Integer, ArrayList<Role>> employeesToRoles = new HashMap<>();
		while (r.next()) {
			int eId = r.getInt("employee_fk");
			ArrayList<Role> roles = employeesToRoles.get(eId);
			if (roles == null) {
				roles = new ArrayList<>();
				employeesToRoles.put(eId, roles);
			}
			roles.add(StaticDataDao.instance.getRole(r.getString("role_fk")));
		}

		r.close();
		s.close();

		s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
		r = s.executeQuery("select id, employee_nr, first_name, last_name, email, internal_cost_center, external_institute, is_external_paid_separately, username, comments from employees");

		while (r.next()) {
			Employee employee = new Employee();
			employee.setId(r.getInt("id"));
			employee.setEmployeeNr(r.getString("employee_nr"));
			employee.setFirstName(r.getString("first_name"));
			employee.setLastName(r.getString("last_name"));
			employee.setEmail(r.getString("email"));
			employee.setInternalCostCenter(r.getInt("internal_cost_center"));
			employee.setExternalInstitute(r.getString("external_institute"));
			employee.setIsPaidSeparately(r.getBoolean("is_external_paid_separately"));
			employee.setUsername(r.getString("username"));
			employee.setComments(r.getString("comments"));
			employee.setRoles(employeesToRoles.get(r.getInt("id")));
			employees.add(employee);
		}
		r.close();
		s.close();

		return employees;
	}

	// TODO implement as hash check...
	public boolean checkPassword(String password) {

		return false;
	}

	public int getNextEmployeeId() throws SQLException {

		int nextId = -1;
		Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();

		ResultSet r = s.executeQuery("select nextval('employees_id_seq');");
		while (r.next()) {
			nextId = r.getInt("nextval");
		}
		r.close();
		s.close();
		return nextId;
	}

	public Employee getEmployeeFirstAndLastNameFor(int employeeId) throws SQLException {
		Employee employee = new Employee();
		employee.setId(employeeId);
		getEmployeeFirstAndLastName.setInt(1, employeeId);
		ResultSet r = getEmployeeFirstAndLastName.executeQuery();
		while (r.next()) {
			employee.setFirstName(r.getString("first_name"));
			employee.setLastName(r.getString("last_name"));
		}
		return employee;
	}

	public ArrayList<Employee> getLecturers() throws SQLException {
		ArrayList<Employee> lecturers = new ArrayList<>();
		ArrayList<Employee> employees = getEmployees();
		for (Employee e : employees) {
			if (!e.getRoles().contains(new Role("Admin", "Administrator"))) {
				lecturers.add(e);
			}
		}
		return lecturers;
	}

	public Employee getEmployeeDetails(int id) throws SQLException {
		getEmployeeById.setInt(1, id);
		ResultSet r = getEmployeeById.executeQuery();
		Employee employee = new Employee();
		while (r.next()) {
			employee.setId(r.getInt("id"));
			employee.setEmployeeNr(r.getString("employee_nr"));
			employee.setFirstName(r.getString("first_name"));
			employee.setLastName(r.getString("last_name"));
			employee.setEmail(r.getString("email"));
			employee.setInternalCostCenter(r.getInt("internal_cost_center"));
			employee.setExternalInstitute(r.getString("external_institute"));
			employee.setIsPaidSeparately(r.getBoolean("is_external_paid_separately"));
			employee.setUsername(r.getString("username"));
			employee.setPassword(null);
			employee.setComments(r.getString("comments"));
		}

		employee.setRoles(getRoles(id));
		r.close();
		return employee;
	}

	private ArrayList<Role> getRoles(int id) throws SQLException {
		ArrayList<Role> roles = new ArrayList<>();
		getRoles.setInt(1, id);
		ResultSet r = getRoles.executeQuery();
		while (r.next()) {
			roles.add(StaticDataDao.instance.getRole(r.getString("role")));
		}
		return roles;
	}

	public boolean authenticate(String username, String password) {
		try {
			return userExistsWithPassword(username, password);
		} catch (SQLException e) { 
			e.printStackTrace();
			return false;
		}
	}

	private boolean userExistsWithPassword(String username, String password) throws SQLException {
		checkUserPw.setString(1, username);
		ResultSet r = checkUserPw.executeQuery();
		String passwordHash = null;
		while (r.next()) {
			passwordHash = r.getString("password");
		}
		if (passwordHash != null) {
			boolean isPwHashValid = PasswordFactory.instance.validatePassword(password, passwordHash);
			if(isPwHashValid){
				CurrentlyLoggedinUsers.instance.addLoggedInUser(getUserForUsername(username));
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	public Employee getUserForUsername(String username) throws SQLException {
		this.getUserForUsername.setString(1, username);
		ResultSet r = this.getUserForUsername.executeQuery();
		while(r.next()){
			Employee employee = getEmployeeDetails(r.getInt("id"));
			return employee;
		}
		r.close(); 
		return null;
	}

	public boolean deleteEmployee(int employeeId) throws SQLException {
		deleteEmployee.setInt(1, employeeId);
		int count = deleteEmployee.executeUpdate();
		return count > 0;
	}

}
