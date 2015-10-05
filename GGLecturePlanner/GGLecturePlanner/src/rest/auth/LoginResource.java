package rest.auth;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.dao.EmployeeDao;
import rest.model.datastructures.Employee;
import rest.model.datastructures.ResponseMessage;

@Path("/login")
public class LoginResource {
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response login(String usernamePassword) {
		// {"username":"ozihler1","password":"*Xnn;``Ed&"}
		String[] uP = usernamePassword.replace("{", "").replace("}", "").split(",");

		if (uP.length != 2) {
			System.out.println("UP not 2 after splittin: " + uP);
		} else {
			Map<String, String> uPs = new HashMap<String, String>();
			for (String p : uP) {
				String[] pa = p.split(":");
				uPs.put(pa[0].replace("\"", ""), pa[1].replace("\"", ""));
			}
			System.out.println("Try to authenticate user: " + uPs.get("username") + " " + uPs.get("password"));
			boolean isAuthenticated = EmployeeDao.instance.authenticate(uPs.get("username"), uPs.get("password"));
			System.out.println((isAuthenticated ? "authenticated: access granted" : "access denied"));
			if (isAuthenticated) {
				try {
					Employee employee = EmployeeDao.instance.getUserForUsername(uPs.get("username"));
					System.out.println(employee);
					return Response.ok(employee, MediaType.APPLICATION_JSON).build();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return Response.ok(new ResponseMessage("could not authorize user with username:" + uPs.get("username"), "denied"),
					MediaType.APPLICATION_JSON).build();
		}
		return Response.ok(new ResponseMessage("Unspecified error occured", "denied"), MediaType.APPLICATION_JSON).build();

	}

	@POST
	@Path("/changepassword")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changePassword(Map<String, Object> pwDetails, @Context HttpServletResponse servletResponse) {
		Integer employeeId = null;
		String username = null;
		String oldPw = null;
		String newPw = null;

		if (pwDetails.get("id") instanceof String) {
			employeeId = Integer.parseInt((String) pwDetails.get("id"));
		} else if (pwDetails.get("id") instanceof Integer) {
			employeeId = (Integer) pwDetails.get("id");
		}
		if (pwDetails.get("username") instanceof String) {
			username = (String) pwDetails.get("username");
		}
		if (pwDetails.get("oldPw") instanceof String) {
			oldPw = (String) pwDetails.get("oldPw");
		}
		if (pwDetails.get("newPw") instanceof String) {
			newPw = (String) pwDetails.get("newPw");
		}
 
		if (!(employeeId == null || username == null || oldPw == null || newPw == null)) {
			boolean isAuthenticated = false;
 			try {
				isAuthenticated = EmployeeDao.instance.userExistsWithPassword(username, oldPw);
 			} catch (SQLException e) {
				e.printStackTrace();
			}
 
			if (isAuthenticated) {
				try {
					EmployeeDao.instance.changePassword(employeeId, username, PasswordFactory.instance.generatePWHash(newPw));
					return Response.ok(new ResponseMessage("Passwort erfolgreich geändert.", "ok"), MediaType.APPLICATION_JSON).build();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return Response.ok(new ResponseMessage("Passwort konnte nicht geändert werden.", "denied"), MediaType.APPLICATION_JSON).build();

	}
}
