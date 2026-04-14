package projet_dw2;

import jakarta.servlet.ServletContext;

public class ParamBD {

	protected static String bdUrl;
	protected static String bdLogin;
	protected static String bdPassword;
	
	protected static void init(ServletContext context) {
		try {
			Class.forName(context.getInitParameter("JDBC_DRIVER"));
			bdUrl = context.getInitParameter("JDBC_URL");
			bdLogin = context.getInitParameter("JDBC_LOGIN");
			bdPassword = context.getInitParameter("JDBC_PASSWORD");
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
