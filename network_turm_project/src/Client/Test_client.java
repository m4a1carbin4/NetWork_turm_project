package Client;


import java.io.*;
import java.net.*;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Test_client {
	
	Socket socket = null;
	InputStream input = null;
	OutputStream output = null;
	DataInputStream datainput = null;
	DataOutputStream dataoutput = null;
	
	BufferedReader in = null;
	
	String str;
	
	JSONParser parser = new JSONParser();
	JSONObject tmp = null;
	
	public Test_client() throws IOException{		
		
		Properties prop = new Properties();
		
		String server ;
		String port ;
		 
		try{
			
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("server.properties"), "UTF-8"));

		    prop.load(in);
		    
		    server = prop.getProperty("server");
			 port = prop.getProperty("port");
		    
		} catch (IOException e){
			server = "localhost";
			port = "9647";
		}	 
		
		socket = new Socket(server,Integer.parseInt(port));
		
		System.out.println("server_connected : "+socket.getLocalAddress());
		
		input = socket.getInputStream();
		output = socket.getOutputStream();
		
		
	}
	
	public JSONObject Json_parser(String data) {
		
		JSONObject json_data = null;
		
		try {
			json_data = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("incoming data error error ");
		}
		
		return json_data;
		
	}
	
	public String Json_maker(String data,String type) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", type);
		object.put("Data", data);
		
		JSONArray arr = new JSONArray();
		
		arr.add(object);
		
		System.out.println("new json data");
		
		return arr.toJSONString();
		
	}
	
	public String Login_maker(String passwd,int ID) {
		
		JSONObject object = new JSONObject();
		
		object.put("ID", ID);
		object.put("passwd", passwd);
		
		JSONArray arr = new JSONArray();
		
		arr.add(object);
		
		System.out.println("new json_login data");
		
		return arr.toJSONString();
	}
	
	public String New_room(String passwd,int ID) {
		
		JSONObject object = new JSONObject();
		
		object.put("room", ID);
		object.put("passwd", passwd);
		
		JSONArray arr = new JSONArray();
		
		arr.add(object);
		
		System.out.println("new json_login data");
		
		return arr.toJSONString();
	}
	
	
	public void start() {
		
		try {
			
			datainput = new DataInputStream(input);
			dataoutput = new DataOutputStream(output);
			in = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("input id :");
			
			int ID = Integer.parseInt(in.readLine());
			
			System.out.println("input id :");
			
			String passwd = in.readLine();
			
			String Login_data = Login_maker(passwd,ID);
			
			String JSON = Json_maker(Login_data,"Login");
			
			dataoutput.writeUTF(JSON);
			

			while(true) {
				
				
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				input.close();
				output.close();;
				datainput.close();
				dataoutput.close();
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
}
