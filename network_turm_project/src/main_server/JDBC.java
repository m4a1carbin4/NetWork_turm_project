package main_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JDBC {
	private static Connection conn = null;

	boolean vaild = false;
	
	public JDBC() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/game_db";
			conn = DriverManager.getConnection(url, "root", "youareadie2!A");

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
	
	public static String Login(String data) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (obj == null)
			return null;
		
		String id = (String)obj.get("ID");
		String pw = (String)obj.get("passwd");
		obj.clear();
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM PLAYER WHERE USER_ID = '" + id + "'"
					+ " AND PASSWORD = hex(aes_encrypt('" + pw
					+ "', 'team2'))";

			rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				obj.put("ID", rs.getInt(1));
				//obj.put("USER_ID", rs.getString(2));
				//obj.put("PW", rs.getString(3));
				obj.put("USER_TYPE", rs.getString(4));
				
				obj.put("NAME", rs.getString(5));
				obj.put("NNAME", rs.getString(6));
				obj.put("EMAIL", rs.getString(7));
				obj.put("PSITE", rs.getString(8));

				obj.put("CONN", rs.getBoolean(9));
				obj.put("CON_DATE", rs.getString(10)); // Last connection date
				obj.put("ROOM_JOINING", rs.getInt(11));
				
				obj.put("WIN", rs.getInt(12));
				obj.put("LOSS", rs.getInt(13));
				
				return obj.toJSONString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean register(String data) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int id = 0;
		
		try {
			var stmt = conn.createStatement();
			String sql = "SELECT MAX(ID) FROM PLAYER";
	
			var rs = stmt.executeQuery(sql);
			
			if (rs.next())
				id = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String uid = (String)obj.get("ID");
		String pw = (String)obj.get("passwd");
		
		String name = (String)obj.get("Name");
		String nname = (String)obj.get("NickName");
		String email = (String)obj.get("EMail");
		String psite = (String)obj.get("PSite");
		
		PreparedStatement pstmt = null;

		try {
			String sql = "INSERT INTO PLAYER VALUES(?, ?, hex(aes_encrypt(?, 'team2')), 1,"
					+ "?, ?, ?, ?,"
					+ "false, null, 0, 0, 0)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, id);
			pstmt.setString(2, uid);
			pstmt.setString(3, pw);
			
			pstmt.setString(4, name);
			pstmt.setString(5, nname);
			pstmt.setString(6, String.valueOf(email));
			pstmt.setString(7, String.valueOf(psite));

			int count = pstmt.executeUpdate();
			if (count != 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean receivechat(String data) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject)parser.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if (obj == null)
			return false;
		
		LocalDate now = LocalDate.now();
		LocalTime time = LocalTime.now();
		
		Formatter fm = new Formatter();
		fm.format("%02d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		DateTimeFormatter datefm = DateTimeFormatter.ofPattern("HH:mm:ss");

		String date = fm.toString() + " " + time.format(datefm);
		String context = (String)obj.get("Data");
		
		PreparedStatement pstmt = null;

		try {
			String sql = "INSERT INTO CHAT VALUES('Unknown', 0, 0, ?, ?, asdf)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, date);
			pstmt.setString(2, context);

			System.out.println(date);
			
			 int count = pstmt.executeUpdate();
			 if (count != 0)
				 return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
