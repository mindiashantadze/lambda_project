package lambda_project.connection_pool;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
	Properties props = new Properties();
	
    private static String url;
    private static String userName;
    private static String password;
	
	private static ConnectionPool instance;
	private BlockingQueue<MockConnection> connections = new LinkedBlockingDeque<>();
	private BlockingQueue<MockConnection> usedConnections = new LinkedBlockingDeque<>();
	
	private ConnectionPool(int size) throws IOException {
		FileInputStream propFilePath = new FileInputStream("src/lambda_project/connection_pool/database.properties");
		props.load(propFilePath);

        url = props.getProperty("url");
        userName = props.getProperty("userName");
        password = props.getProperty("password");
        
        if (url.equals(null) || userName.equals(null) || password.equals(null)) {
        	throw new RuntimeException("Please specify the following properties: url, userName, password");
        }

		
		for (int i = 0; i < size; i++) {
			try {
				MockConnection connection = MockDriverManager.getConnection(url, userName, password); 
				connections.offer(connection);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public synchronized MockConnection getConnection() {
		MockConnection connection = null;
		try {
			connection = connections.take();
			usedConnections.offer(connection);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
		return connection;
	}


	public synchronized boolean releaseConnection(MockConnection connection) {
        connections.add(connection);
        return usedConnections.remove(connection);
    }
	
	
	
	public static ConnectionPool init(int size) throws IOException {
		if (instance == null) {
			instance = new ConnectionPool(size);
		}
		
		return instance; 
	}
}
