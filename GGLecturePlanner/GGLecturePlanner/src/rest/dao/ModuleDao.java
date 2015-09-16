package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Department;
import rest.model.datastructures.Discipline;
import rest.model.datastructures.Module;
import rest.model.datastructures.ModuleType;

public enum ModuleDao {
	instance;

	private PreparedStatement insertModule;
	private PreparedStatement insertPlansToModule;
	private PreparedStatement insertModuleNrs;
	private PreparedStatement insertModuleTypes;
	private PreparedStatement insertDepartmentsToModules;
	private PreparedStatement insertModulesToDisciplines;
	private PreparedStatement getDepartmentForModule;
	private PreparedStatement getDisciplinesForModule;
	private PreparedStatement getModuleTypesForModule;
	private PreparedStatement getModuleNrsForModule;
	private PreparedStatement getModuleNrs;
	private PreparedStatement getModuleDetails;

	private ModuleDao() {
		try {
			this.insertModule = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into modules values(?,?,?,?,?,?)");
			this.insertPlansToModule = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into plans_to_modules values(?,?)");
			this.insertModuleNrs = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into module_nrs values(?,?)");
			this.insertModuleTypes = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into modules_to_module_types values(?,?)");

			this.insertDepartmentsToModules = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into departments_to_modules values(?,?)");

			this.insertModulesToDisciplines = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("insert into modules_to_disciplines values(?,?)");
			
			this.getModuleDetails = DBConnectionProvider.instance.prepareStatement("select * from modules where id = ?");

			this.getDepartmentForModule = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"select distinct d.* from departments_to_modules dm inner join departments d on dm.dept_id_fk = d.id where dm.module_id_fk=?;");
			this.getDisciplinesForModule = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"select distinct  d.* from disciplines d inner join  modules_to_disciplines md on d.abbr=md.discipline_fk where md.module_id_fk=?;");
			this.getModuleTypesForModule = DBConnectionProvider.instance
					.getDataSource()
					.getConnection()
					.prepareStatement(
							"select distinct m.* from modules_to_module_types mm inner join module_types m on mm.module_type_fk = m.abbr where mm.module_id_fk=?;");
			this.getModuleNrsForModule = DBConnectionProvider.instance.getDataSource().getConnection()
					.prepareStatement("select distinct module_nr from module_nrs where module_id_fk=?;");
		 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addModule(int planId, Module module) throws SQLException {

		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);
		int moduleId = module.getId();
		this.insertModule.setInt(1, moduleId);
		this.insertModule.setString(2, module.getSemesterNr());
		this.insertModule.setString(3, module.getAssessmentType().getAbbreviation());
		this.insertModule.setString(4, module.getAssessmentDate());
		this.insertModule.setInt(5, module.getResponsibleEmployee().getId());
		this.insertModule.setString(6, module.getComments());

		this.insertModule.executeUpdate();

		this.insertDepartmentsToModules.setInt(1, moduleId);
		this.insertDepartmentsToModules.setInt(2, module.getDepartment().getId());

		this.insertDepartmentsToModules.executeUpdate();
		for (String moduleNr : module.getPrimaryNrs()) {
			this.insertModuleNrs.setInt(1, moduleId);
			this.insertModuleNrs.setString(2, moduleNr);
			this.insertModuleNrs.executeUpdate();
		}

		for (ModuleType mt : module.getModuleTypes()) {
			System.out.println(moduleId);
			this.insertModuleTypes.setInt(1, moduleId);
			this.insertModuleTypes.setString(2, mt.getAbbreviation());
			this.insertModuleTypes.executeUpdate();
		}
		this.insertPlansToModule.setInt(1, planId);
		this.insertPlansToModule.setInt(2, moduleId);
		this.insertPlansToModule.executeUpdate();

		for (Discipline discipline : module.getDisciplines()) {
			this.insertModulesToDisciplines.setInt(1, moduleId);
			this.insertModulesToDisciplines.setString(2, discipline.getAbbreviation());
			this.insertModulesToDisciplines.executeUpdate();
		}
		DBConnectionProvider.instance.getDataSource().getConnection().commit();
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);

	}

	public int getNextModuleId() {

		int nextId = -1;
		try {
			Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r = s.executeQuery("select nextval('modules_id_seq');");
			while (r.next()) {
				nextId = r.getInt("nextval");
			}
			r.close();
			s.close();
		} catch (SQLException s) {
			s.getStackTrace();
		}
		return nextId;
	}
 

	public Department getDepartments(Integer id) throws SQLException {
		Department dept = null;

		getDepartmentForModule.setInt(1, id);
		ResultSet r = getDepartmentForModule.executeQuery();
		while (r.next()) {
			dept = new Department(r.getInt("id"), r.getString("dept_name"), r.getString("field_of_expertise"));
		}
		return dept;
	}

	public ArrayList<Discipline> getDisciplines(Integer id) throws SQLException {

		ArrayList<Discipline> disciplines = new ArrayList<Discipline>();

		getDisciplinesForModule.setInt(1, id);
		ResultSet r = getDisciplinesForModule.executeQuery();
		while (r.next()) {
			disciplines.add(new Discipline(r.getString("abbr"), r.getString("description")));
		}
		return disciplines;
	}

	public ArrayList<String> getPrimaryNrs(Integer id) throws SQLException {
		ArrayList<String> primaryNrs = new ArrayList<String>();

		getModuleNrsForModule.setInt(1, id);
		ResultSet r = getModuleNrsForModule.executeQuery();
		while (r.next()) {
			primaryNrs.add(r.getString("module_nr"));
		}
		return primaryNrs;
	}

	public ArrayList<ModuleType> getModuleTypes(Integer id) throws SQLException {
		ArrayList<ModuleType> moduleTypes = new ArrayList<ModuleType>();

		getModuleTypesForModule.setInt(1, id);
		ResultSet r = getModuleTypesForModule.executeQuery();
		while (r.next()) {
			moduleTypes.add(new ModuleType(r.getString("abbr"), r.getString("description")));
		}
		return moduleTypes;
	}

 
	public Module getModuleDetails(int moduleId) throws SQLException{

		Module module = new Module();

		getModuleDetails.setInt(1, moduleId);
		ResultSet r = getModuleDetails.executeQuery();
		
		while(r.next()){
			module.setId(r.getInt("id"));
			module.setSemesterNr(r.getString("semester_nr"));
			module.setAssessmentType(StaticDataDao.instance.getAssessmentType(r.getString("assessment_type_fk")));
			module.setAssessmentDate(r.getString("assessment_date"));
			module.setResponsibleEmployee(EmployeeDao.instance.getEmployeeFirstAndLastNameFor(r.getInt("responsible_employee")));
			module.setComments(r.getString("comments"));
		}
		r.close();
		
		module.setDepartment(getDepartments(moduleId));
		module.setModuleTypes(getModuleTypes(moduleId));
		module.setPrimaryNrs(getPrimaryNrs(moduleId));
		module.setDisciplines(getDisciplines(moduleId));
		return module;
	}
	
}
