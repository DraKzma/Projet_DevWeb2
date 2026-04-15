package projet_dw2;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init() {
		ParamBD.init(this.getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			//L'utilisateur connectee est automatiquement redirigee vers DocumentChoice
			response.sendRedirect(request.getContextPath() + "/DocumentChoice");
		}
		else {
			//L'utilisateur non connectee est transportee sur l'index permettant de se connecter ou s'inscire
			request.setAttribute("error", 0);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/SignUp.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//Si formulaire non rempli
		if((username.equals("")) || (password.equals(""))) {
			request.setAttribute("error", 1);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/SignUp.jsp");
			rd.forward(request, response);
		}
		//Si formulaire rempli
		else {
			if(User.SignUp(username, password)) {
				//Si l'inscription s'est bien passee
				System.out.println("User: " + username + " is successfully signed up");
				response.sendRedirect(request.getContextPath() + "/SignIn");
			}
			else {
				//Si l'inscription ne s'est pas bien passee
				request.setAttribute("error", 2);
				RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/SignUp.jsp");
				rd.forward(request, response);
			}
		}
	}

}
