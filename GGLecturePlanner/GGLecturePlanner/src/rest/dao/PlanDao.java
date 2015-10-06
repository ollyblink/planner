package rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rest.model.datastructures.Course;
import rest.model.datastructures.CourseTimesAndRooms;
import rest.model.datastructures.Employee;
import rest.model.datastructures.IAbbrDescr;
import rest.model.datastructures.Module;
import rest.model.datastructures.Plan;
import rest.model.datastructures.Role;
import rest.model.datastructures.Times;

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

		this.deletePlanStatement = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("delete from plans where id=?;");
		this.updatePlan = DBConnectionProvider.instance.getDataSource().getConnection()
				.prepareStatement("update plans set semester=?, year=? where id=?;");

		this.existsPlanId = DBConnectionProvider.instance.getDataSource().getConnection().prepareStatement("select * from plans where id=?");
	}

	public ArrayList<Plan> getAllPlans() throws SQLException {
		ArrayList<Plan> plans = new ArrayList<>();

		Statement s = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
		ResultSet r = s.executeQuery("select * from plans");

		while (r.next()) {
			int planid = r.getInt("id");
			Plan plan = new Plan(planid, r.getString("semester"), r.getInt("year"), null);

			ArrayList<Module> modules = new ArrayList<>();
			Statement s1 = DBConnectionProvider.instance.getDataSource().getConnection().createStatement();
			ResultSet r1 = s1.executeQuery("select distinct module_id_fk from plans_to_modules where plan_id_fk = " + planid);
			while (r1.next()) {
				Module module = new Module();
				module.setId(r1.getInt("module_id_fk"));
				modules.add(module);
			}
			r1.close();
			s1.close();

			plan.setModules(modules);
			plans.add(plan);

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
		while (r.next()) {
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

		String content = "<h1>Lehraufträge " + plan.getSemester() + " " + plan.getYear() + "</h1>" + "<table>" + "<tr>" + "<th>VVZNr</th>"
				+ "<th>Modul</th>" + "<th>Modulteil</th>" + "<th>Semester</th>" + "<th>Disziplin</th>" + "<th>Bezeichnung der Veranstaltung</th>"
				+ "<th>Vorname Nachname</th>" + "<th>Funktion an der Uni bei internen DozentInnen (Prof, PD, OA, Wimi)</th>"
				+ "<th>externe DozentInnen (ETH, PSI, WSL, ...)</th>" + "<th>externe DozentInnen (x)</th>"
				+ "<th>Kostenstelle Abt.? - nicht MNF?</th><th>Kommentare zu Dozenten</th>"
				+ "<th>Verteilung über Semester / Veranstaltungs-rhytmus</th>" + "<th>Start</th>" + "<th>Beginn-Datum</th>"
				+ "<th>End-Datum falls nicht ganzes Semester</th>" + "<th>Typ Veranstaltung (VL, UE, Exkursion, Blockkurs, ...)</th>"
				+ "<th># Gruppen</th>" + "<th>Anzahl Studenten erwartet pro Gruppe</th>" + "<th>SWS tot. pro Gruppe</th>"
				+ "<th>Anz. Dozenten (inkl. Profs.)</th>" + "<th>SWS/Doz. Präsenzzeit</th>"
				+ "<th>Bemerkungen (o.k. = keine Änderungen, aktualisiert, nicht mehr gültig)</th>" + "<th>Prüfungen</th>" + "<th>Wochentag</th>"
				+ "<th>Zeit</th>" + "<th>Gewünschter Hörsaal / Seminarraum</th>" + "<th>Kapazität des gew. Raumes</th>"
				+ "<th>Kommentare zu Räumen & Zeiten</th>" + "<th>Eintrag von (Kürzel)</th><th>Kommentare zum Modul</th></tr>";

		for (Module module : plan.getModules()) {
			for (Course course : module.getCourses()) {
				content += "<tr>" + "<td>" + course.getVvzNr() + "</td>" + "<td>" + formatList(module.getPrimaryNrs()) + "</td>" + "<td>"
						+ formatList(course.getModuleParts()) + "</td>" + "<td>" + module.getSemesterNr() + "</td>" + "<td>"
						+ formatAbbrDescrList(module.getDisciplines()) + "</td>" + "<td>" + course.getCourseDescription() + "</td>"
						+ formatLecturers(course.getLecturers()) + "<td>" + switchRythm(course.getRythm()) + "</td>" + "<td>"
						+ switchDate(course.getDate()) + "</td>" + "<td>" + course.getStartDate() + "</td>" + "<td>" + course.getEndDate() + "</td>"
						+ "<td>" + course.getCourseType().getAbbreviation() + "</td>" + "<td>" + course.getNrOfGroups() + "</td>" + "<td>"
						+ course.getNrOfStudentsExpectedPerGroup() + " " + (course.getIsMaxNrStudentsExpectedPerGroup() ? "(max)" : "") + "</td>"
						+ "<td>" + course.getSwsTotalPerGroup() + "</td>" + "<td>" + course.getLecturers().size() + "</td><td>"
						+ (course.getSwsTotalPerGroup() / course.getLecturers().size()) + "</td>" + "<td>" + course.getComments() + "</td>" + "<td>"
						+ module.getAssessmentType().getAbbreviation() + " " + module.getAssessmentDate() + "</td>"

						+ formatTimesAndRooms(course.getCourseTimesAndRooms()) + "<td>"
						+ module.getResponsibleEmployee().getFirstName().substring(0, 1)  
						+ module.getResponsibleEmployee().getLastName().substring(0, 1) + "</td><td>" + module.getComments() + "</td></tr>";

			}
		}

		content += "</table>";
		String html = getHTMLBasics(planId, content);
		return html;
	}

	private String switchRythm(String rythm) {
		switch (rythm) {
		case "1":
			return "wöchentlich";
		case "2":
			return "14-täglich";
		case "3":
			return "unregelmässig";
		default:
			return "";
		}
	}

	private String switchDate(String date) {
		switch (date) {
		case "1":
			return "1. Sem. Woche";
		case "2":
			return "2. Sem. Woche";
		case "3":
			return "Siehe Daten";
		default:
			return "";
		}
	}

	private String formatTimesAndRooms(ArrayList<CourseTimesAndRooms> courseTimesAndRooms) {
		String timesAndRooms = "<td><ol>";
		for (CourseTimesAndRooms cTR : courseTimesAndRooms) {
			timesAndRooms += "<li>" + cTR.getDayOfWeek() + "</li>";
		}
		timesAndRooms += "</ol></td>";
		timesAndRooms += "<td><ol>";
		for (CourseTimesAndRooms cTR : courseTimesAndRooms) {
			Times times = cTR.getTimes();
			if (times != null) {
				timesAndRooms += "<li>" + times.getStartTime() + " - " + times.getEndTime() + "</li>";
			}
		}
		timesAndRooms += "</ol></td>";
		timesAndRooms += "<td><ol>";
		for (CourseTimesAndRooms cTR : courseTimesAndRooms) {
			timesAndRooms += "<li>" + cTR.getRoomLabel() + "</li>";
		}
		timesAndRooms += "</ol></td>";
		timesAndRooms += "<td><ol>";
		for (CourseTimesAndRooms cTR : courseTimesAndRooms) {
			timesAndRooms += "<li>" + cTR.getRoomCapacity() + "</li>";
		}
		timesAndRooms += "</ol></td>";
		timesAndRooms += "<td><ol>";
		for (CourseTimesAndRooms cTR : courseTimesAndRooms) {
			timesAndRooms += "<li>" + cTR.getComments() + "</li>";
		}
		timesAndRooms += "</ol></td>";
		return timesAndRooms;
	}

	private String formatLecturers(ArrayList<Employee> lecturers) {
		String formattedLecturers = "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + l.getFirstName() + " " + l.getLastName() + "</li>";
		}
		formattedLecturers += "</ol></td>";
		formattedLecturers += "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + formatRoles(l.getRoles()) + "</li>";
		}
		formattedLecturers += "</ol></td>";

		formattedLecturers += "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + l.getExternalInstitute() + "</li>";
		}
		formattedLecturers += "</ol></td>";
		formattedLecturers += "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + (l.getIsPaidSeparately().getValue() ? "x" : "") + "</li>";
		}
		formattedLecturers += "</ol></td>";
		formattedLecturers += "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + (l.getInternalCostCenter() == null ? "" : l.getInternalCostCenter()) + "</li>";
		}
		formattedLecturers += "</ol></td>";
		formattedLecturers += "<td><ol>";
		for (Employee l : lecturers) {
			formattedLecturers += "<li>" + (l.getComments()) + "</li>";
		}
		formattedLecturers += "</ol></td>";
		return formattedLecturers;
	}

	private String formatRoles(ArrayList<Role> roles) {
		String roleString = "";
		for (int i = 0; i < roles.size() - 1; ++i) {
			roleString += roles.get(i).getAbbreviation() + ",";
		}
		if (roles.size() > 0) {
			roleString += roles.get(roles.size() - 1).getAbbreviation();
		}
		return roleString;
	}

	private <T> String formatList(ArrayList<T> list) {
		String formattedData = "";
		for (int i = 0; i < list.size() - 1; ++i) {
			formattedData += list.get(i).toString() + "\n";
		}
		if (list.size() > 0) {
			formattedData += list.get(list.size() - 1).toString();
		}
		return formattedData;
	}

	private <T extends IAbbrDescr> String formatAbbrDescrList(ArrayList<T> list) {
		String formattedData = "";
		for (int i = 0; i < list.size() - 1; ++i) {
			formattedData += list.get(i).getAbbreviation() + "\n";
		}
		if (list.size() > 0) {
			formattedData += list.get(list.size() - 1).getAbbreviation();
		}
		return formattedData;
	}

	private String getHTMLBasics(int planId, String content) {
		return "<html>" + "<head><title>Plan " + planId + "</title>" + "<style>" + "      body,div, table, tr, td, p { " + "font-family: arial;"
				+ "font-size: 6px; " + "text-align: left;" + "vertical-align: top;" + "}" + "ol {  margin: 0;  padding: 0; }" + "    </style>"
				+ "</head>" + "<body><div class=\"container\" >" + content + "</div></body>" + "</html>";
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
