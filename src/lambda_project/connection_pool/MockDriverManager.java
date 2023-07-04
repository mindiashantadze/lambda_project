package lambda_project.connection_pool;

public class MockDriverManager {
	public static MockConnection getConnection(String url, String userName, String password) {
		return new MockConnection();
	}
}
