package projet_dw2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Editor")
public class Editor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String user_role = ""; 
	
	public void init() {
		ParamBD.init(this.getServletContext());
	}
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String docName = request.getParameter("doc");
		
		if(user != null) {
			//L'utilisateur connectee peut acceder à la page
			int retour = reviwewRole(user, docName);
			if(retour == 0) {
				//Tout va bien
				request.setAttribute("role", user_role);
				RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
				rd.forward(request, response);
			}
			else if(retour == 1) {
				//Document n'existe pas
				System.out.println("pas de doc");
				response.sendRedirect(request.getContextPath() + "/DocumentChoice");
			}
			else {
				//pas d'access
				System.out.println("pas d'access");
				response.sendRedirect(request.getContextPath() + "/DocumentChoice");
			}
		}
		else {
			//L'utilisateur non connectee est transportee sur l'index permettant de se connecter ou s'inscire
			response.sendRedirect(request.getContextPath() + "/");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//Fonction qui verifie le role de l'utilisateur pour ce document
	/*
	 * Valeurs de retours:
	 * 0: l'utilisateur a acces à ce document
	 * 1: le document n'existe pas
	 * 2: l'utilisateur n'as pas access à ce document
	 */
	public int reviwewRole(User user, String docName) {
		String role = "";
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			//Recuperation du document du meme nom
			String sql = "SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				//Le document n'existe pas dans la base
				return 1;
			}
			int document_id = rs.getInt("id");
			
			//Verification du droit d'acces de l'utilisateur
			sql = "SELECT role"
				+ " FROM hasAccessTo"
				+ " WHERE user_id = ?"
				+ " AND document_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1,user.getId()); pst.setInt(2, document_id);
			rs = pst.executeQuery();
			if(!rs.next()) {
				//L'utilisateur n'as pas acces au document
				return 2;
			}
			role = rs.getString("role");
			rs.close(); pst.close(); connexion.close(); //fermetures 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		user_role = role;
		return 0;
	}

}
