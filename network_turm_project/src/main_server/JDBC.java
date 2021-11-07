package main_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	private static Connection conn = null;

	boolean vaild = false;
	
	public JDBC() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://192.168.35.165:3306/game_db";
			conn = DriverManager.getConnection(url, "root", "12345");

			System.out.println("[JDBC] Connected with: " + url);
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot found the driver");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		
		vaild = true;
	}
	
	public void finalize() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean Login(String data) {
		// Parse string into data required
		String id = "administrator";
		String pw = "admin_password_teamb";
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String sql = "SELECT USER_ID FROM PLAYER WHERE USER_ID = '" + id + "'"
					+ " AND PASSWORD = hex(aes_encrypt('" + pw
					+ "', 'team2'))";

			rs = stmt.executeQuery(sql);

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean register(String data) {
		// ...
		String id = "id", pw = "pw";
		
		PreparedStatement pstmt = null;

		try {
			String sql = "INSERT INTO PLAYER VALUES(?, hex(aes_encrypt(?, 'team2')), 0,"
					+ "'Name', 'NName', null, null,"
					+ "false, null, 0, 0, 0)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, pw);

			int count = pstmt.executeUpdate();
			if (count != 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
}
