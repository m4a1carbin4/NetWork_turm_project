module server {
	requires com.google.gson;
	requires json.simple;
	requires java.desktop;
	requires java.sql;
	
	exports main_server;
	exports Client;
	opens main_server;
}