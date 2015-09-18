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

	private PreparedStatement moduleExists;
	
	private PreparedStatement insertModule;
	private PreparedStatement insertPlansToModule;
	private PreparedStatement insertModuleNrs;
	private PreparedStatement insertModuleTypes;
	private PreparedStatement insertDepartmentsToModules;
	private PreparedStatement insertModulesToDisciplines;

	private PreparedStatement updateModule;  
	private PreparedStatement updateDepartmentsToModules;
	private PreparedStatement deleteModuleTypes; 
	private PreparedStatement deleteModulesToDisciplines;
	private PreparedStatement deleteModuleNr;

	private PreparedStatement getDepartmentForModule;
	private PreparedStatement getDisciplinesForModule;
	private PreparedStatement getModuleTypesForModule;
	private PreparedStatement getModuleNrsForModule;
	private PreparedStatement getModuleDetails;

	private PreparedStatement deleteModule;

	private ModuleDao() {

		try {
			createUtilityStatements();
			createInsertStatements(); 
			createUpdateStatements(); 
			createDeleteStatements();  
			createGetDataStatements();
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createUtilityStatements() throws SQLException {
		this.moduleExists = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("select id from modules where id=?");
	}

	private void createDeleteStatements() throws SQLException {
		this.deleteModuleTypes = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("delete from modules_to_module_types where module_id_fk=? and module_type_fk=?;"); 
		this.deleteModulesToDisciplines = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("delete from modules_to_disciplines where module_id_fk = ? and discipline_fk = ?;");

		this.deleteModuleNr = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("delete from module_nrs where module_nr=? and module_id_fk = ?;");
		
		this.deleteModule = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("delete from modules where id = ?;");
	}

	private void createUpdateStatements() throws SQLException {
		this.updateModule = DBConnectionProvider.instance
				.getDataSource()
				.getConnection()
				.prepareStatement(
						"update modules set semester_nr=?, assessment_type_fk=?, assessment_date=?, responsible_employee=?, comments=? where id=?;");
 
		this.updateDepartmentsToModules = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("update departments_to_modules set dept_id_fk=? where module_id_fk = ?;");
	}

	private void createGetDataStatements() throws SQLException {
		this.getModuleDetails = DBConnectionProvider.instance.prepareStatement("select * from modules where id = ?;");

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
	}

	private void createInsertStatements() throws SQLException {
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
	}

	public void addModule(int planId, Module module) throws SQLException {

		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);
		
		int moduleId = module.getId(); 
		insertModule(module, moduleId);
		insertPlansToModules(planId, moduleId);
		
		insertDepartmentsToModules(module, moduleId);
		insertModuleNrs(module, moduleId);
		insertModuleTypes(module, moduleId);
		insertModulesToDisciplines(module, moduleId);
		
		DBConnectionProvider.instance.getDataSource().getConnection().commit();
		DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);

	}
	
	public void updateModule(int planId, Module module) throws SQLException {

		int moduleId = module.getId();

		if (moduleExists(moduleId)) {
			DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(false);
			//For comparison...
			Module oldModule = getModuleDetails(module.getId()); 
			
			updateModule(module, moduleId);  
			updateDepartmentsToModule(module, moduleId);
			//No update plans to modules... makes no sense so far as you cannot change the module from one to another plan...
			updateModuleNrs(module, moduleId, oldModule);
			updateModuleTypes(module, moduleId, oldModule); 
			updateModulesToDisciplines(module, moduleId, oldModule);
			
			DBConnectionProvider.instance.getDataSource().getConnection().commit();
			DBConnectionProvider.instance.getDataSource().getConnection().setAutoCommit(true);
		}

	}
	private void insertModule(Module module, int moduleId) throws SQLException {
		this.insertModule.setInt(1, moduleId);
		this.insertModule.setString(2, module.getSemesterNr());
		this.insertModule.setString(3, module.getAssessmentType().getAbbreviation());
		this.insertModule.setString(4, module.getAssessmentDate());
		this.insertModule.setInt(5, module.getResponsibleEmployee().getId());
		this.insertModule.setString(6, module.getComments());

		this.insertModule.executeUpdate();
	}

	private void insertDepartmentsToModules(Module module, int moduleId) throws SQLException {
		
		Department dept = module.getDepartment();
		if(dept != null){
			this.insertDepartmentsToModules.setInt(1, moduleId);
			this.insertDepartmentsToModules.setInt(2, dept.getId()); 
			this.insertDepartmentsToModules.executeUpdate();
		}
		 
	}

	private void insertPlansToModules(int planId, int moduleId) throws SQLException {
		this.insertPlansToModule.setInt(1, planId);
		this.insertPlansToModule.setInt(2, moduleId);
		this.insertPlansToModule.executeUpdate();
	}

	private void insertModuleTypes(Module module, int moduleId) throws SQLException {
		for (ModuleType mt : module.getModuleTypes()) {
			System.out.println(moduleId);
			this.insertModuleTypes.setInt(1, moduleId);
			this.insertModuleTypes.setString(2, mt.getAbbreviation());
			this.insertModuleTypes.executeUpdate();
		}
	}

	private void insertModulesToDisciplines(Module module, int moduleId) throws SQLException {
		for (Discipline discipline : module.getDisciplines()) {
			this.insertModulesToDisciplines.setInt(1, moduleId);
			this.insertModulesToDisciplines.setString(2, discipline.getAbbreviation());
			this.insertModulesToDisciplines.executeUpdate();
		}
	}

	private void insertModuleNrs(Module module, int moduleId) throws SQLException {
		
		for (String moduleNr : module.getPrimaryNrs()) {
			if(moduleNr == null || moduleNr.length() == 0){
				continue;
			}
 			this.insertModuleNrs.setInt(1, moduleId);
			this.insertModuleNrs.setString(2, moduleNr);
			this.insertModuleNrs.executeUpdate();
		}
	}



	private void deleteModuleTypes(Module module, int moduleId) throws SQLException {
		for (ModuleType mt : module.getModuleTypes()) { 
			this.deleteModuleTypes.setInt(1, moduleId);
			this.deleteModuleTypes.setString(2, mt.getAbbreviation());
			this.deleteModuleTypes.executeUpdate();
		}
	}

	private void updateDepartmentsToModule(Module module, int moduleId) throws SQLException {
		this.updateDepartmentsToModules.setInt(1, module.getDepartment().getId());  
		this.updateDepartmentsToModules.setInt(2, moduleId);
		this.updateDepartmentsToModules.executeUpdate();
	}

	private void updateModule(Module module, int moduleId) throws SQLException {
		this.updateModule.setString(1, module.getSemesterNr());
		this.updateModule.setString(2, module.getAssessmentType().getAbbreviation());
		this.updateModule.setString(3, module.getAssessmentDate());
		this.updateModule.setInt(4, module.getResponsibleEmployee().getId());
		this.updateModule.setString(5, module.getComments());
		this.updateModule.setInt(6, moduleId);

		this.updateModule.executeUpdate();
	}

	private void updateModuleNrs(Module updatedModule, int moduleId, Module oldModule) throws SQLException {
		//delete the old ones 
		deleteModuleNrs(oldModule, moduleId);
		insertModuleNrs(updatedModule, moduleId);
	}
	private void updateModulesToDisciplines(Module updatedModule, int moduleId, Module oldModule) throws SQLException {
		deleteModulesToDisciplines(oldModule, moduleId);
		insertModulesToDisciplines(updatedModule, moduleId);
	}


	private void updateModuleTypes(Module updatedModule, int moduleId, Module oldModule) throws SQLException {
		deleteModuleTypes(oldModule, moduleId); 
		insertModuleTypes(updatedModule, moduleId);
	}
	
	private void deleteModulesToDisciplines(Module oldModule, int moduleId) throws SQLException {
		for (Discipline discipline : oldModule.getDisciplines()) {
			this.deleteModulesToDisciplines.setInt(1, moduleId);
			this.deleteModulesToDisciplines.setString(2, discipline.getAbbreviation());
			this.deleteModulesToDisciplines.executeUpdate();
		}
	}


	private void deleteModuleNrs( Module oldModule,int moduleId) throws SQLException {
		for (String moduleNr : oldModule.getPrimaryNrs()) {
			 deleteModuleNr.setString(1, moduleNr);
			 deleteModuleNr.setInt(2, moduleId);
			 deleteModuleNr.executeUpdate();
		}
	}

	private boolean moduleExists(int moduleId) throws SQLException {
		moduleExists.setInt(1, moduleId);
		ResultSet r = moduleExists.executeQuery();
		while(r.next()){
			return true;
		}
		return false;
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

	public Module getModuleDetails(int moduleId) throws SQLException {

		Module module = new Module();

		getModuleDetails.setInt(1, moduleId);
		ResultSet r = getModuleDetails.executeQuery();

		while (r.next()) {
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
		module.setCourses(new ArrayList<>());
		return module;
	}

	public boolean deleteModule(int moduleId) throws SQLException {
		deleteModule.setInt(1, moduleId);
		int update = deleteModule.executeUpdate();
		return (update > 0);
	}

}
