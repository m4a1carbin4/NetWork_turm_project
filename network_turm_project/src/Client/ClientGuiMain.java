package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientGuiMain {
	public static void main(String[] args) {
		
		
		Scanner input = new Scanner(System.in);
		String ip_input = null;
		
		System.out.println("input ip string 000.000.000.000 (local = -1)");
		
		ip_input = input.next();
		
		if(ip_input.equals("-1")) {
			try {
				InetAddress ia = InetAddress.getLocalHost(); 
				String ip_str = ia.toString();
				String ip = ip_str.substring(ip_str.indexOf("/") + 1);
				new MainFrame(ip, 9647);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}else {
			String ip = ip_input;
			new MainFrame(ip, 9647);
		}
		/*
		try {
			InetAddress ia = InetAddress.getLocalHost(); 
			String ip_str = ia.toString();
			String ip = ip_str.substring(ip_str.indexOf("/") + 1);
			new MainFrame(ip, 9647);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
		
	}
}
