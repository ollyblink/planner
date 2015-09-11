package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Department;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Module;
import rest.model.datastructures.ModuleType;
import rest.model.datastructures.Plan;

public enum PlanDao {
	instance;

	private PreparedStatement getPlanDetails;
	private PreparedStatement addPlanStatement; 

	private PlanDao() {
		try {
			getPlanDetails = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"select p.id as planid, p.semester, p.year, m.id as moduleid, m.* from plans p inner join plans_to_modules pm on p.id = pm.plan_id_fk inner join modules m on pm.module_id_fk = m.id where p.id = ?;");

			this.addPlanStatement = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into plans(semester, year) values (?,?);");
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Plan> getAllPlans() throws SQLException {
		ArrayList<Plan> plans = new ArrayList<>();

		Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
		ResultSet r = s.executeQuery("select * from plans");

		while (r.next()) {
			plans.add(new Plan(r.getInt("id"), r.getString("semester"), r.getInt("year"), new ArrayList<>()));
		}
		r.close();
		s.close();

		return plans;
	}

	public Plan getPlanDetailsFor(int planId) throws SQLException {

		Plan plan = null;
		getPlanDetails.setInt(1, planId);
		ResultSet r = getPlanDetails.executeQuery();
		while (r.next()) {

			if (plan == null) {
				plan = new Plan(r.getInt("planid"), r.getString("semester"), r.getInt("year"), new ArrayList<>());
			}

			Employee responsibleEmployee = new Employee();
			responsibleEmployee.setId(r.getInt("responsible_employee"));

			Module module = new Module(r.getInt("moduleid"), null, r.getString("semester_nr"), StaticTypesDao.instance.getAssessmentType(r
					.getString("assessment_type_fk")), r.getString("assessment_date"), responsibleEmployee, r.getString("comments"), null, null,
					null, null);

			plan.getModules().add(module);
		}
		r.close();

		System.out.println("Plan: " + plan);
		if (plan != null) {
			ArrayList<Module> modules = plan.getModules();

			for (Module m : modules) {
				m.setModuleTypes(ModuleDao.instance.getModuleTypes(m.getId()));
				m.setPrimaryNrs(ModuleDao.instance.getPrimaryNrs(m.getId()));
				m.setDisciplines(ModuleDao.instance.getDisciplines(m.getId()));
				m.setDepartment(ModuleDao.instance.getDepartments(m.getId()));
				m.setResponsibleEmployee(EmployeeDao.instance.getEmployeeFirstAndLastNameFor(m.getResponsibleEmployee().getId()));
				System.out.println("Modules for plan: " + plan.getId() + ": " + m);
			}

		}
		return plan;

	}

	 

	public void addPlan(String semester, int year) throws SQLException {
		addPlanStatement.setString(1, semester);
		addPlanStatement.setInt(2, year);
		addPlanStatement.executeUpdate();

	}
}
