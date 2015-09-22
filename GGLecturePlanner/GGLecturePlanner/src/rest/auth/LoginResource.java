package rest.auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.dao.EmployeeDao;

@Path("/login")
public class LoginResource {
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response login(String usernamePassword) {
		// {"username":"ozihler1","password":"*Xnn;``Ed&"}
		System.out.println(usernamePassword);
		String[] uP = usernamePassword.replace("{", "").replace("}", "").split(",");
		for (String s : uP) {
			System.out.println(s);
		}
		if (uP.length != 2) {
			System.out.println("UP not 2 after splittin: " + uP);
		} else {
			Map<String, String> uPs = new HashMap<String, String>();
			for (String p : uP) {
				String[] pa = p.split(":");
				uPs.put(pa[0].replace("\"", ""), pa[1].replace("\"", ""));
			}
			System.out.println(uPs.get("username") + " " + uPs.get("password"));
			System.out.println("Auth: " + EmployeeDao.instance.authenticate(uPs.get("username"), uPs.get("password")));
			if (EmployeeDao.instance.authenticate(uPs.get("username"), uPs.get("password"))) {
				try {
					return Response.ok(EmployeeDao.instance.getUserForUsername(uPs.get("username")), MediaType.APPLICATION_JSON).build();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			return Response.seeOther(new URI("../index.html")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
