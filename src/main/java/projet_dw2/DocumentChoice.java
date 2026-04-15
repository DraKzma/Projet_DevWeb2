package projet_dw2;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DocumentChoice")
public class DocumentChoice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init() {
		ParamBD.init(this.getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			//L'utilisateur connectee est automatiquement redirigee vers DocumentChoice
			request.setAttribute("error", 0);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/DocumentChoice.jsp");
			rd.forward(request, response);
		}
		else {
			//L'utilisateur non connectee est transportee sur l'index permettant de se connecter ou s'inscire
			response.sendRedirect(request.getContextPath() + "/");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String docName = request.getParameter("DocName");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(docName.equals("")) {
			//Si le formulaire n'est pas rempli
			request.setAttribute("error", 1);
			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/DocumentChoice.jsp");
			rd.forward(request, response);
		}
		
		else {
			//Si le formumaire est rempli
			if(request.getParameter("create") != null) {
				/*
				 * Des choses à faire..
				 */
				System.out.println("User: " + user.getUsername() + " created document " + docName + " successfully.");
				response.sendRedirect(request.getContextPath() + "/Editor?doc=" + docName);
			}
			else if(request.getParameter("find") != null) {
				/*
				 * Des choses à faire..
				 */
				response.sendRedirect(request.getContextPath() + "/Editor?doc=" + docName);
			}
			else {
				//Cas d'erreur bizzare qui ne devrait pas arriver en theorie
				System.out.println("DocumentChoice: doPost detected but no form button was previously clicked.");
			}
		}
	}

}
