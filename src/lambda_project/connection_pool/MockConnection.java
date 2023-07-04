package lambda_project.connection_pool;

import java.util.ArrayList;
import java.util.List;

public class MockConnection {
	public String getData() {
		return "Thread " + Thread.currentThread().getId() + " data";
	}
	
	public static String getConnection(String url, String username, String password) {
		System.out.println("url: " + url);
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		
		System.out.println("DB Connected");
		
		
		return "DB Connected";
	}
}
