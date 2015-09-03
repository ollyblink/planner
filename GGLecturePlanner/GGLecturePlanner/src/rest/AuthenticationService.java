package rest;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.StringTokenizer;

public class AuthenticationService {

	private Map<String, String> users;

	public AuthenticationService(Map<String, String> users) {
		this.users = users;
	}

	public boolean authenticate(String authCredentials) {

		if (null == authCredentials) {
			return false;
		}
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		System.out.println("Received: "+ usernameAndPassword);
		if(!users.containsKey(username)){
			return false;
		}
		System.out.println("Contains: " +username);
		// we have fixed the userid and password as admin
		// call some UserService/LDAP here
		System.out.println("with password: " +password+": " +users.get(username).equals(password));
		return users.get(username).equals(password);
	 
	}
}