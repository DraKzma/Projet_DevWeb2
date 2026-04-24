package projet_dw2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/Editor")
public class Editor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String user_role = ""; 
	private static final String CHEMIN_FICHIERS = "/ficProjetWeb/";
	private static final String EXTENSION_FICHIERS = ".txt";
	
	HashMap<String, String> userList; //Liste des users ayant access au document ainsi que leur role
	
	public void init() {
		ParamBD.init(this.getServletContext());
	}
	
	public void printUserList() {
		for(String username : userList.keySet()) {
			System.out.println(username + " " + userList.get(username));
		}
	}
	
	public void setUserList(String docName) {
		this.userList = new HashMap<String, String>();
		
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			String sql = " SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				System.out.println("ERROR. Le document n'existe pas mais vous vous trouvez sur la page.");
			}
			
			int document_id = rs.getInt("id");
			sql = " SELECT user_id, role"
				+ " FROM hasAccessTo"
				+ " WHERE document_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id);
			rs = pst.executeQuery();
			
			int user_id; String role; ResultSet rstmp; String username;
			while(rs.next()) {
				user_id = rs.getInt("user_id"); role = rs.getString("role");
				
				sql = " SELECT username"
					+ " FROM users"
					+ " WHERE id = ?;";
				pst = connexion.prepareStatement(sql);
				pst.setInt(1, user_id);
				rstmp = pst.executeQuery();
				if(!rstmp.next()) {
					System.out.println("ERROR. User in hasAccessTo table but not in users table.");
				}
				username = rstmp.getString("username");
				
				userList.put(username, role);
				rstmp.close();
			}
			
			rs.close(); pst.close(); connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				request.setAttribute("error", 0);
				
				setUserList(docName);
				printUserList();
				request.setAttribute("userList", userList);
				
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
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String docName = request.getParameter("doc");
		
		reviwewRole(user, docName);
		request.setAttribute("role", user_role);
		
		setUserList(docName);
		printUserList();
		request.setAttribute("userList", userList);
		
		if(request.getParameter("ban") != null || request.getParameter("viewer") != null || request.getParameter("writer") != null) {
			
			String username = request.getParameter("username");
			
			if(username.equals("")) {
				//Si le formulaire n'est pas rempli
				request.setAttribute("error", 1);
				RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
				rd.forward(request, response);
			}
			else if(request.getParameter("ban") != null) {
				int retour = ban(username, docName, user.getUsername());
				if(retour == 0) {
					request.setAttribute("error", 2);
					
					setUserList(docName);
					printUserList();
					request.setAttribute("userList", userList);
					
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if (retour == 1) {
					request.setAttribute("error", 3);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if(retour == 2) {
					request.setAttribute("error", 4);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else {
					request.setAttribute("error", 5);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
			}
			else if(request.getParameter("viewer") != null) {
				int retour = setToViewer(username, docName, user.getUsername());
				if(retour == 0) {
					request.setAttribute("error", 6);
					
					setUserList(docName);
					printUserList();
					request.setAttribute("userList", userList);
					
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if (retour == 1) {
					request.setAttribute("error", 7);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if(retour == 2) {
					request.setAttribute("error", 8);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else {
					request.setAttribute("error", 9);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
			}
			else {
				int retour = setToWriter(username, docName, user.getUsername());
				if(retour == 0) {
					request.setAttribute("error", 10);
					
					setUserList(docName);
					printUserList();
					request.setAttribute("userList", userList);
					
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if (retour == 1) {
					request.setAttribute("error", 11);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else if(retour == 2) {
					request.setAttribute("error", 12);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
				else {
					request.setAttribute("error", 13);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/Editor.jsp");
					rd.forward(request, response);
				}
			}
		}
		
		else if(request.getParameter("delete") != null){
			deleteDocument(docName);
			response.sendRedirect(request.getContextPath() + "/DocumentChoice");
		}
		else if(request.getParameter("save") != null) {
			doGet(request, response);
		}
		else if(request.getParameter("download") != null){
			doGet(request, response);
		}
		else {
			//Erreur bizzare qui ne devrait pas se produire
			System.out.println("ERROR, doPost detected but no form was previously submitted.");
		}
	}
	
	//Fonction qui supprime un document de la base
	/*
	 * Valeurs de retour:
	 * 0: tout s'est bien passee
	 * 1: le document n'existe pas
	 */
	public int deleteDocument(String docName) {
		
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			String sql = " SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				//Document n'existe pas
				return 1;
			}
			
			int document_id = rs.getInt("id");
			sql = "DELETE FROM documents"
				+ " WHERE id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id);
			pst.executeUpdate();
			
			File dossier = new File(System.getProperty("user.home") + CHEMIN_FICHIERS);
			File fichier = new File(dossier.getAbsolutePath() + "/" + docName + EXTENSION_FICHIERS);
			if(fichier.exists()) {
				if(!fichier.delete()) {
					System.out.println("ERROR. Impossible de supprimer le fichier du serveur.");
				}
			}
			
			rs.close(); pst.close(); connexion.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	//Fonction qui donne le role viewer à un user pour ce document
	/*
	 * Valeurs de retours:
	 * 0: promotion effectuee avec succes
	 * 1: le user n'existe pas
	 * 2: le user est vous meme
	 * 3: le user est deja un writer
	 */
	public int setToWriter(String username, String docName, String owner) {
		
		if(username.equals(owner)) {
			return 2;
		}
		
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			String sql = "SELECT id"
					+ " FROM users"
					+ " WHERE username = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				return 1;
			}
			
			int user_id = rs.getInt("id");
			sql = " SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			rs = pst.executeQuery();
			if(!rs.next()) {
				System.out.println("ERROR. Le document n'existe pas mais vous vous trouvez sur la page.");
			}
			
			int document_id = rs.getInt("id");
			sql = " SELECT role"
				+ " FROM hasAccessTo"
				+ " WHERE document_id = ?"
				+ " AND user_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id); pst.setInt(2, user_id);
			rs = pst.executeQuery();
			if(rs.next()) {
				if(rs.getString("role").equals("WRITER")) {
					return 3;
				}
				else {
					//Il faut faire une UPDATE
					
					sql = " UPDATE hasAccessTo"
						+ " SET role = \'WRITER\'"
						+ " WHERE user_id = ?"
						+ " AND document_id = ?;";
					pst = connexion.prepareStatement(sql);
					pst.setInt(1, user_id); pst.setInt(2, document_id);
					pst.executeUpdate();
				}
			}
			else {
				//Il faut faire un INSERT INTO
				
				sql = " INSERT INTO hasAccessTo(user_id, document_id, role)"
					+ " VALUES (?, ?, \'WRITER\');";
				pst = connexion.prepareStatement(sql);
				pst.setInt(1, user_id); pst.setInt(2, document_id);
				pst.executeUpdate();
			}
			
			rs.close(); pst.close(); connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	//Fonction qui donne le role viewer à un user pour ce document
	/*
	 * Valeurs de retours:
	 * 0: promotion effectuee avec succes
	 * 1: le user n'existe pas
	 * 2: le user est vous meme
	 * 3: le user est deja un viewer
	 */
	public int setToViewer(String username, String docName, String owner) {
		
		if(username.equals(owner)) {
			return 2;
		}
		
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			String sql = "SELECT id"
					+ " FROM users"
					+ " WHERE username = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				return 1;
			}
			
			int user_id = rs.getInt("id");
			sql = " SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			rs = pst.executeQuery();
			if(!rs.next()) {
				System.out.println("ERROR. Le document n'existe pas mais vous vous trouvez sur la page.");
			}
			
			int document_id = rs.getInt("id");
			sql = " SELECT role"
				+ " FROM hasAccessTo"
				+ " WHERE document_id = ?"
				+ " AND user_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id); pst.setInt(2, user_id);
			rs = pst.executeQuery();
			if(rs.next()) {
				if(rs.getString("role").equals("VIEWER")) {
					return 3;
				}
				else {
					//Il faut faire une UPDATE
					
					sql = " UPDATE hasAccessTo"
						+ " SET role = \'VIEWER\'"
						+ " WHERE user_id = ?"
						+ " AND document_id = ?;";
					pst = connexion.prepareStatement(sql);
					pst.setInt(1, user_id); pst.setInt(2, document_id);
					pst.executeUpdate();
				}
			}
			else {
				//Il faut faire un INSERT INTO
				
				sql = " INSERT INTO hasAccessTo(user_id, document_id, role)"
					+ " VALUES (?, ?, \'VIEWER\');";
				pst = connexion.prepareStatement(sql);
				pst.setInt(1, user_id); pst.setInt(2, document_id);
				pst.executeUpdate();
			}
			
			rs.close(); pst.close(); connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	//Fonction qui ban un user de ce document
	/*
	 * Valeurs de retours:
	 * 0: ban effectuee avec succes
	 * 1: le user n'existe pas
	 * 2: le user est vous meme
	 * 3: le user est deja banni
	 */
	public int ban(String username, String docName, String owner) {
		
		if(username.equals(owner)) {
			return 2;
		}
		
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			String sql = "SELECT id"
					+ " FROM users"
					+ " WHERE username = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if(!rs.next()) {
				return 1;
			}
			
			int user_id = rs.getInt("id");
			sql = " SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setString(1, docName);
			rs = pst.executeQuery();
			if(!rs.next()) {
				System.out.println("ERROR. Le document n'existe pas mais vous vous trouvez sur la page.");
			}
			
			int document_id = rs.getInt("id");
			sql = " SELECT role"
				+ " FROM hasAccessTo"
				+ " WHERE document_id = ?"
				+ " AND user_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id); pst.setInt(2, user_id);
			rs = pst.executeQuery();
			if(!rs.next()) {
				return 3;
			}
			
			sql = " DELETE FROM hasAccessTo"
				+ " WHERE document_id = ?"
				+ " AND user_id = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setInt(1, document_id); pst.setInt(2, user_id);
			pst.executeUpdate();
			
			rs.close(); pst.close(); connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
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
