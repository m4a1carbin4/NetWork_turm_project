package Json_Controller;

import java.util.Hashtable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json_Controller {
	private static JSONParser parser = new JSONParser();
	
	public static JSONObject parse(String data) {
		
		JSONObject json_data = null;
		
		try {
			json_data = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("incoming data error error ");
		}
		
		return json_data;
		
	}
	
	public static String make(Hashtable<String, String> value) {
		
		JSONObject object = new JSONObject();
		
		for (var iter = value.keySet().iterator(); iter.hasNext(); ) {
			String key = iter.next();
			object.put(key, value.get(key));
		}
		
		return object.toJSONString();
		
	}
	
	public static String wrap(String type, String data) {
		if (type == null)
			return null;
		
		if (data == null)
			data = "";
		
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		result.put("Type", type);
		result.put("Data", data);
		
		return make(result);
	}
}
