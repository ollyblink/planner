package rest.auth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rest.dao.EmployeeDao;

public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			
			StringBuilder sb = new StringBuilder();
			sb.append(" [");
			sb.append("\n\t request.getContextPath(): ").append(httpServletRequest.getContextPath());
			sb.append("\n\t request.getLocalAddr(): ").append(httpServletRequest.getLocalAddr());
			sb.append("\n\t request.getLocalName(): ").append(httpServletRequest.getLocalName());
			sb.append("\n\t request.getLocalPort(): ").append(httpServletRequest.getLocalPort());
			sb.append("\n\t request.getPathInfo(): ").append(httpServletRequest.getPathInfo());
			sb.append("\n\t request.getPathTranslated(): ").append(httpServletRequest.getPathTranslated());
			sb.append("\n\t request.getProtocol(): ").append(httpServletRequest.getProtocol());
			sb.append("\n\t request.getRemoteAddr(): ").append(httpServletRequest.getRemoteAddr());
			sb.append("\n\t request.getRemoteHost(): ").append(httpServletRequest.getRemoteHost());
			sb.append("\n\t request.getRemotePort(): ").append(httpServletRequest.getRemotePort());
			sb.append("\n\t request.getRequestURI(): ").append(httpServletRequest.getRequestURI());
			sb.append("\n\t request.getRequestURL(): ").append(httpServletRequest.getRequestURL());
			sb.append("\n\t request.getScheme(): " ).append(httpServletRequest.getScheme());
			sb.append("\n\t request.getServerName(): ").append(httpServletRequest.getServerName());
			sb.append("\n\t request.getServerPort(): ").append(httpServletRequest.getServerPort());
			sb.append("\n\t request.getServletPath(): ").append(httpServletRequest.getServletPath());
			sb.append("\n]");
			
			System.out.println(sb.toString());
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);
 
			System.out.println("Auth credentials: "+ authCredentials);
			StringTupel uPW = getUsernameAndPassword(authCredentials);
			System.out.println(uPW);
 
			boolean isAuth = false;
			try {
				isAuth = CurrentlyLoggedinUsers.instance.containsUser(EmployeeDao.instance.getUserForUsername(uPW.username));
			} catch (SQLException e) { 
				e.printStackTrace();
			}
			System.out.println("Is authenticated?" +isAuth);
			if (isAuth) {
				if (response instanceof HttpServletResponse) { 
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
				} 
				filter.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;					
					httpServletResponse.sendRedirect("/GGLecturePlanner/index.html");
 				}			
				

			}
		}
	 
	}

	private class StringTupel {
		public String username;
		public String password;

		public StringTupel(String u, String p) {
			username = u;
			password = p;
		}

		@Override
		public String toString() {
			return "StringTupel [username=" + username + ", password=" + password + "]";
		}
		
		
	}

	private StringTupel getUsernameAndPassword(String authCredentials) {
		if (null == authCredentials) {
			return new StringTupel("","");
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
		return new StringTupel(username, password);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
