package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rest.model.datastructures.Employee;
import rest.model.datastructures.Role;

public enum EmployeeDao {
	instance;

	private PreparedStatement addEmployeeStatement;
	private PreparedStatement addEmployeeRolesStatement;
	private PreparedStatement getEmployeeFirstAndLastName;
	private PreparedStatement getEmployeeById;
	private PreparedStatement getRoles;

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
			
			this.getEmployeeById =  DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select e.* from employees e where e.id= ?;");
			
			this.getRoles = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select role_fk as role from employees_to_roles where employee_fk=?;");

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
	public void addEmployee(Employee employee) throws SQLException {

		// TODO: Throw away password and put there some hash instead...

		System.out.println("ServceDao:addEmployee: employeeNr: " + employee.getEmployeeNr());
		this.addEmployeeStatement.setInt(1, employee.getId());
		this.addEmployeeStatement.setString(2, employee.getEmployeeNr());
		this.addEmployeeStatement.setString(3, employee.getFirstName());
		this.addEmployeeStatement.setString(4, employee.getLastName());
		this.addEmployeeStatement.setString(5, employee.getEmail());
		this.addEmployeeStatement.setInt(6, employee.getInternalCostCenter());
		this.addEmployeeStatement.setString(7, employee.getExternalInstitute());
		this.addEmployeeStatement.setBoolean(8, employee.getIsPaidSeparately());
		this.addEmployeeStatement.setString(9, employee.getUsername());
		this.addEmployeeStatement.setString(10, employee.getPassword());
		this.addEmployeeStatement.setString(11, employee.getComments());

		this.addEmployeeStatement.executeUpdate();

		for (Role role : employee.getRoles()) {
			System.out.println(employee + " " + role);
			this.addEmployeeRolesStatement.setInt(1, employee.getId());
			this.addEmployeeRolesStatement.setString(2, role.getAbbreviation());
			this.addEmployeeRolesStatement.executeUpdate();
		}

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
		for(Employee e: employees) {
			if(!e.getRoles().contains(new Role("Admin", "Administrator"))){
				lecturers.add(e);
			}
		}
		return lecturers;
	}

	public Employee getEmployeeDetails(int id) throws SQLException {
		getEmployeeById.setInt(1, id);
		ResultSet r = getEmployeeById.executeQuery();
		Employee employee = new Employee();
		while(r.next()){
			employee.setId(r.getInt("id"));
			employee.setEmployeeNr(r.getString("employee_nr"));
			employee.setFirstName(r.getString("first_name"));
			employee.setLastName(r.getString("last_name"));
			employee.setEmail(r.getString("email"));
			employee.setInternalCostCenter(r.getInt("internal_cost_center"));
			employee.setExternalInstitute(r.getString("external_institute"));
			employee.setIsPaidSeparately(r.getBoolean("is_external_paid_separately"));
			employee.setUsername(r.getString("username"));
			employee.setPassword(r.getString("password"));
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
		while(r.next()){
			roles .add(StaticDataDao.instance.getRole(r.getString("role")));
		}
		return roles;
	}
 

}
