package projet_dw2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	private static final String CHEMIN_FICHIERS = "/ficProjetWeb/";
	private static final String EXTENSION_FICHIERS = ".txt";
	
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
				if(createDocument(docName, user)) {
					System.out.println("User: " + user.getUsername() + " created document " + docName + " successfully.");
					response.sendRedirect(request.getContextPath() + "/Editor?doc=" + docName);
				}
				else {
					//Document du même nom existe déjà
					request.setAttribute("error", 2);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/DocumentChoice.jsp");
					rd.forward(request, response);
				}
			}
			else if(request.getParameter("find") != null) {
				/*
				 * Des choses à faire..
				 */
				int retour = findDocument(docName, user);
				if(retour == 0) {
					response.sendRedirect(request.getContextPath() + "/Editor?doc=" + docName);
				}
				else if (retour == 1) {
					//Document introuvable
					request.setAttribute("error", 3);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/DocumentChoice.jsp");
					rd.forward(request, response);
				}
				else {
					//L'utilisateur n'a pas acces à ce document
					request.setAttribute("error", 4);
					RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/DocumentChoice.jsp");
					rd.forward(request, response);
				}
			}
			else {
				//Cas d'erreur bizzare qui ne devrait pas arriver en theorie
				System.out.println("DocumentChoice: doPost detected but no form button was previously clicked.");
			}
		}
	}
	
	//Fonction qui cherche le document dans la base de données
	/*
	 * Valeurs de retour:
	 * 0: tout s'est bien passee
	 * 1: le document n'a pas pu être trouvee
	 * 2: le document existe mais l'utilisateur n'y a pas acces
	 */
	public int findDocument(String documentName, User user) {
		try {
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			//Recuperation du document du meme nom
			String sql = "SELECT id"
					+ " FROM documents"
					+ " WHERE name = ?;";
			PreparedStatement pst = connexion.prepareStatement(sql);
			pst.setString(1, documentName);
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
			rs.close(); pst.close(); connexion.close(); //fermetures 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	//Fonction qui ajoute le document à la base de données et rempli hasAccessTo
	public void addDocumentToDatabase(String documentName, User user) {
		//Connection à la base
		Connection connexion;
		try {
			connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			//Insertion du fichier dans la base
			String sql = "INSERT INTO documents (id, name)"
					+ " VALUES (NULL, ?);";
			PreparedStatement pst = connexion.prepareStatement(sql);
			String n = documentName;
			pst.setString(1, n);
			pst.executeUpdate();
			
			//Recuperation de l'id du document qu'on vient de creer
			sql = "SELECT id"
				+ " FROM documents"
				+ " WHERE name = ?;";
			pst = connexion.prepareStatement(sql);
			pst.setString(1, n);
			ResultSet rs = pst.executeQuery();
			int document_id = 0;
			while(rs.next()) {
				document_id = rs.getInt("id");
			}
			
			//Selection de tout les users pour remplir hasAccessTo
			sql = "SELECT id, permissions"
				+ " FROM users;";
			Statement st  = connexion.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				if(rs.getInt("id") == user.getId()) {
					//L'user qui vient de creer le document est affecter en tant que OWNER
					sql = "INSERT INTO hasAccessTo (user_id, document_id, role)"
					+ " VALUES (?, ?, \'OWNER\');";
					pst = connexion.prepareStatement(sql);
					pst.setInt(1, rs.getInt("id")); pst.setInt(2, document_id);
					pst.executeUpdate();
				}
				else {
					//Sinon c'est un simple VIEWER
					sql = "INSERT INTO hasAccessTo (user_id, document_id, role)"
					+ " VALUES (?, ?, \'VIEWER\');";
					pst = connexion.prepareStatement(sql);
					pst.setInt(1, rs.getInt("id")); pst.setInt(2, document_id);
					pst.executeUpdate();
				}
			}
			
			rs.close(); pst.close(); st.close(); connexion.close(); //fermetures
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Fonction qui crée le document vide dans la base de données et dans les fichiers du serveur
	public boolean createDocument(String documentName, User user) {
		try {
			File dossier = new File(System.getProperty("user.home") + CHEMIN_FICHIERS);
			System.out.println(dossier.getAbsolutePath());
			dossier.mkdir();
			File fichier = new File(dossier.getAbsolutePath() + "/" + documentName + EXTENSION_FICHIERS);
			if(fichier.createNewFile()) {
				addDocumentToDatabase(documentName, user);
			}
			else {
				System.out.println("Fichier de même non déjà existant.");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
