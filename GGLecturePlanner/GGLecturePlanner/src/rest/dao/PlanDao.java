package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Employee;
import rest.model.datastructures.Module;
import rest.model.datastructures.Plan;

public enum PlanDao {
	instance;

	private PreparedStatement getPlanDetails;
	private PreparedStatement addPlanStatement;
	private PreparedStatement deletePlanStatement;
	private PreparedStatement updatePlan; 
	private PreparedStatement getPlan;

	private PlanDao() {
		try {
			getPlan = DBConnectionProvider.instance.prepareStatement("select * from plans where id=?");
			getPlanDetails = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"select p.id as planid, p.semester, p.year, m.id as moduleid, m.* from plans p inner join plans_to_modules pm on p.id = pm.plan_id_fk inner join modules m on pm.module_id_fk = m.id where p.id = ?;");

			this.addPlanStatement = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into plans(semester, year) values (?,?);");
			
			
			this.deletePlanStatement = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("delete from plans where id=?;");
			this.updatePlan = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("update plans set semester=?, year=? where id=?;");
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
		getPlan.setInt(1, planId);
		
		ResultSet planRs = getPlan.executeQuery();
		while(planRs.next()){
			plan = new Plan(planRs.getInt("id"), planRs.getString("semester"), planRs.getInt("year"), new ArrayList<>());
		}
		planRs.close();
		
		getPlanDetails.setInt(1, planId);
		ResultSet r = getPlanDetails.executeQuery();
		while (r.next()) { 

			Employee responsibleEmployee = new Employee();
			responsibleEmployee.setId(r.getInt("responsible_employee"));

			Module module = new Module(r.getInt("moduleid"), null, r.getString("semester_nr"), StaticDataDao.instance.getAssessmentType(r
					.getString("assessment_type_fk")), r.getString("assessment_date"), responsibleEmployee, r.getString("comments"), null, null,
					null, null);

			plan.getModules().add(module);
		}
		r.close();

		System.out.println("PlanDao: getPlanDetailsFor: plan: " + plan);
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

	public boolean deletePlan(int planId) throws SQLException {
		deletePlanStatement.setInt(1, planId);
		int update = deletePlanStatement.executeUpdate();
		return (update > 0);
	}

	public boolean changePlan(int planId, String semester, int year) throws SQLException {
		System.out.println("PlanDao:changePlan:planId: "+ planId +",semester:"+semester+",year:"+year);
		updatePlan.setString(1, semester);
		updatePlan.setInt(2, year);
		updatePlan.setInt(3, planId);
		
		return (updatePlan.executeUpdate() > 0);
		
	}
}
