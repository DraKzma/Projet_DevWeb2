package projet_dw2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import projet_dw2.User;

public class User {

	private int id;
	private String username;
	private int permissions; //1: user classique & 2: admin
	
	public User(int id, String username) {
		this.id = id;
		this.username = username;
		this.permissions = 1;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getPermissions() {
		return this.permissions;
	}
	
	//Fonction pour se connecter, retourne l'utilisateur connectee, null si erreur
	public static User SignIn(String username, String password) {
		int id = -1; 
		try { 
			//Connection à la base 
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword); 
			
			//Creation de la requete preparee 
			String sql = "SELECT id" 
			+ " FROM users" 
			+ " WHERE username = ?" 
			+ " AND password = ?" 
			+ ";"; 
			PreparedStatement pst = connexion.prepareStatement(sql); 
			String l = username; String m = password; //pour recup les ? 
			pst.setString(1,l); pst.setString(2, m); 
			ResultSet rs = pst.executeQuery(); 
			while(rs.next()) { 
				id = rs.getInt("id"); 
			} 
			rs.close(); pst.close(); connexion.close(); //fermetures 
			if(id == -1) { 
				return null; 
			} 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} 
		return new User(id,username);
	}
	
	//Fonction pour s'inscrire, retourne true si tout s'est bien passee et false sinon
	public static boolean SignUp(String username, String password) {
		try {
			//Connection à la base
			Connection connexion = DriverManager.getConnection(ParamBD.bdUrl, ParamBD.bdLogin, ParamBD.bdPassword);
			
			//Creation de la requete sql
			String sql = "SELECT id"
					+ "	FROM users"
					+ "	WHERE username = ?"
					+ ";";
			PreparedStatement pst = connexion.prepareStatement(sql);
			String l = username; //pour recup les ?
			pst.setString(1,l);
			ResultSet rs = pst.executeQuery();
			
			if(!(rs.next())) {
				//Creation de la requete sql
				sql = "INSERT INTO users (id, username, password, permissions)"
						+ " VALUES (NULL, ?, ?, 1);";
				pst = connexion.prepareStatement(sql);
				String m = password;
				pst.setString(1, l); pst.setString(2, m);
				pst.executeUpdate();
			}
			else {
				System.out.println("ERREUR, il existe déjà un utilisateur du même nom dans la base.");
				rs.close(); pst.close(); connexion.close(); //fermetures
				return false;
			}
			
			rs.close(); pst.close(); connexion.close(); //fermetures
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}
}
