package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Course;
import rest.model.datastructures.CourseTimesAndRooms;
import rest.model.datastructures.Employee;
import rest.model.datastructures.Module;
import rest.model.datastructures.Plan;
import rest.model.datastructures.Role;

public enum PlanDao {
	instance;

	private PreparedStatement getModulesForPlan;
	private PreparedStatement addPlanStatement;
	private PreparedStatement deletePlanStatement;
	private PreparedStatement updatePlan;
	private PreparedStatement getPlan;
	private PreparedStatement existsPlanId;

	private PlanDao() {
		try {
			prepareStatements();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void prepareStatements() throws SQLException {
		getPlan = DBConnectionProvider.instance.prepareStatement("select * from plans where id=?");
		getModulesForPlan = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("select distinct module_id_fk as moduleid from plans_to_modules p where p.plan_id_fk =  ?;");

		this.addPlanStatement = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("insert into plans(id, semester, year) values (?,?,?);");

		this.deletePlanStatement = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("delete from plans where id=?;");
		this.updatePlan = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("update plans set semester=?, year=? where id=?;");

		this.existsPlanId = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("select * from plans where id=?");
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
		if (existsPlanId(planId)) {
			return createPlanFromDBEntries(planId);
		} else {
			return null;
		}
	}

	private boolean existsPlanId(int planId) throws SQLException {
		existsPlanId.setInt(1, planId);
		ResultSet r = existsPlanId.executeQuery();
		while(r.next()){
			return true;
		}
		return false;
	}

	private Plan createPlanFromDBEntries(int planId) throws SQLException {
		Plan plan = getPlanFromDB(planId);
		getModulesForPlanFromDB(plan);
		return plan;
	}

	private void getModulesForPlanFromDB(Plan plan) throws SQLException {
		getModulesForPlan.setInt(1, plan.getId());

		ResultSet r = getModulesForPlan.executeQuery();

		while (r.next()) {
			Module module = ModuleDao.instance.getModuleDetails(r.getInt("moduleid"));
			plan.getModules().add(module);
		}

		r.close();
	}

	private Plan getPlanFromDB(int planId) throws SQLException {
		Plan plan = null;
		getPlan.setInt(1, planId);

		ResultSet planRs = getPlan.executeQuery();
		while (planRs.next()) {
			plan = new Plan(planRs.getInt("id"), planRs.getString("semester"), planRs.getInt("year"), new ArrayList<>());
		}
		planRs.close();
		return plan;
	}

	public int addPlan(String semester, int year) throws SQLException {
		int nextPlanId = getNextPlanId();
		addPlanStatement.setInt(1, nextPlanId);
		addPlanStatement.setString(2, semester);
		addPlanStatement.setInt(3, year);
		addPlanStatement.executeUpdate();
		return nextPlanId;
	}

	public int getNextPlanId() {

		int nextId = -1;
		try {
			Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r = s.executeQuery("select nextval('plans_id_seq');");
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

	public boolean deletePlan(int planId) throws SQLException {
		deletePlanStatement.setInt(1, planId);
		int update = deletePlanStatement.executeUpdate();
		return (update > 0);
	}

	public boolean changePlan(int planId, String semester, int year) throws SQLException {
		System.out.println("PlanDao:changePlan:planId: " + planId + ",semester:" + semester + ",year:" + year);
		updatePlan.setString(1, semester);
		updatePlan.setInt(2, year);
		updatePlan.setInt(3, planId);

		return (updatePlan.executeUpdate() > 0);

	}

	public void copyPlan(int planIdOfPlanToCopy) throws SQLException {
		Plan fullPlan = retrieveFullPlan(planIdOfPlanToCopy);

		fullPlan.setYear(fullPlan.getYear() + 1); // Simply add one as it probably is one year later anyways...
		System.out.println("FULL PLAN\n" + fullPlan);

		int newPlanId = addPlan(fullPlan.getSemester(), fullPlan.getYear());

		for (Module module : fullPlan.getModules()) {
			System.out.println("Module: " + module);
			module.setId(ModuleDao.instance.getNextModuleId());
			ModuleDao.instance.addModule(newPlanId, module);

			for (Course course : module.getCourses()) {

				course.setId(CourseDao.instance.getNextCourseId());
				course.setModuleNr(module.getId());

				for (CourseTimesAndRooms cTR : course.getCourseTimesAndRooms()) {
					cTR.setId(CourseDao.instance.getNextCourseRoomsAndTimesId());
					cTR.setCourseId(course.getId());
					cTR.setModuleId(module.getId());
				}
				CourseDao.instance.addCourse(course);
			}
		}
	}

	public String createHTMLPage(int planId) throws SQLException {
		Plan plan = retrieveFullPlan(planId);

		String content = "<h1>Lehrauftr�ge " + plan.getSemester() + " " + plan.getYear() + "</h1>" + "<table>" + "<tr>" + "<th>VVZNr</th>"
				+ "<th>Modul</th>" + "<th>Modulteil</th>" + "<th>Semester</th>" + "<th>Disziplin</th>" + "<th>Bezeichnung der Veranstaltung</th>"
				+ "<th>Vorname Nachname</th>" + "<th>Funktion an der Uni bei internen DozentInnen (Prof, PD, OA, Wimi)</th>"
				+ "<th>externe DozentInnen (ETH, PSI, WSL, ...)</th>" + "<th>externe DozentInnen (x)</th>" + "<th>Anz. Gruppen</th>"
				+ "<th>Wochentag</th>" + "<th>Zeit</th>" + "<th>Verteilung �ber Semester / Veranstaltung-rhytmus</th>" + "<th>Beginn-Datum</th>"
				+ "<th>End-Datum falls nicht ganzes Semester</th>" + "<th>Typ Veranstaltung (VL, UE, Exkursion, Blockkurs, ...)</th>"
				+ "<th>Anzahl Studenten erwartet pro Gruppe</th>" + "<th>Gew�nschter H�rsaal / Semnarraum</th>"
				+ "<th>Kapazit�t des gew. Raumes</th>" + "<th>SWS tot. pro Gruppe</th>" + "<th>Anz. Dozenten (inkl. Profs.)</th>"
				+ "<th>SWS/Doz. Pr�senzzeit</th>" + "<th>Kostenstelle Abt.? - nicht MNF?</th>"
				+ "<th>Bemerkungen (o.k. = keine �nderungen, aktualisiert, nicht mehr g�ltig)</th>" + "<th>Pr�fungen</th>"
				+ "<th>Eintrag von (K�rzel)</th>" + "</tr>";

		for (Module module : plan.getModules()) {
			for (Course course : module.getCourses()) {
				content += "<tr>" + "<td>" + course.getVvzNr() + "</td>" + "<td>" + formatList(module.getPrimaryNrs()) + "</td>" + "<td>"
						+ formatList(course.getModuleParts()) + "</td>" + "<td>" + module.getSemesterNr() + "</td>" + "<td>"
						+ formatList(module.getDisciplines()) + "</td>" + "<td>" + course.getCourseDescription() + "</td>"
						+ formatLecturers(course.getLecturers())
						// + "<td>"++"</td>"
						// + "<td>"++"</td>"
						// + "<td>"++"</td>"
						// + "<td>"++"</td>"
						// + "<td>"++"</td>"
						// + "<td>"++"</td>"
						+ "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "</tr>";
				for (CourseTimesAndRooms cTR : course.getCourseTimesAndRooms()) {
				}
			}
		}

		content += "</table>";
		String html = getHTMLBasics(planId, content);
		return html;
	}

	private String formatLecturers(ArrayList<Employee> lecturers) {
		String formattedLecturers = "";
		for (Employee l : lecturers) {
			formattedLecturers += "<td>" + l.getFirstName() + " " + l.getLastName() + "</td>";
			formattedLecturers += "<td>" + formatRoles(l.getRoles()) + "</td>";
			formattedLecturers += "<td>" + l.getExternalInstitute() + "</td>";
			formattedLecturers += "<td>" + (l.getIsPaidSeparately().getValue() ? "x" : "") + "</td>";
			formattedLecturers += "<td>" + (l.getInternalCostCenter() == null ? "" : l.getInternalCostCenter()) + "</td>";
		}
		return formattedLecturers;
	}

	private String formatRoles(ArrayList<Role> roles) {
		String roleString = "";
		for (int i = 0; i < roles.size() - 1; ++i) {
			roleString += roles.get(i).getAbbreviation() + ",";
		}
		roleString += roles.get(roles.size() - 1).getAbbreviation();
		return roleString;
	}

	private <T> String formatList(ArrayList<T> list) {
		String formattedData = "";
		for (int i = 0; i < list.size() - 1; ++i) {
			formattedData += list.get(i).toString() + "\n";
		}
		formattedData += list.get(list.size() - 1).toString();
		return formattedData;
	}

	private String getHTMLBasics(int planId, String content) {
		return "<html>" + "<head><title>Plan " + planId + "</title><link rel=\"stylesheet\" type=\"text/css\" href=\"../../../style.css\" /></head>"
				+ "<body><div class=\"container\" >" + content + "</div></body>" + "</html>";
	}

	private Plan retrieveFullPlan(int planId) throws SQLException {
		Plan plan = getPlanDetailsFor(planId);
		ArrayList<Module> modules = plan.getModules();
		for (Module module : modules) {
			module.setCourses(CourseDao.instance.getCoursesForModule(module.getId()));
			for (Course course : module.getCourses()) {
				ArrayList<Employee> lecturers = course.getLecturers();
				ArrayList<Employee> fullBlownLecturers = new ArrayList<>();

				for (Employee e : lecturers) {
					fullBlownLecturers.add(EmployeeDao.instance.getEmployeeDetails(e.getId()));
				}

				course.setLecturers(fullBlownLecturers);

			}
		}
		return plan;
	}

}
