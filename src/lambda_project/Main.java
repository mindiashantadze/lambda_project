package lambda_project;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lambda_project.connection_pool.ConnectionPool;
import lambda_project.connection_pool.MockConnection;

public class Main {
	
	public static void main(String args[]) throws Exception {
		final int POOL_SIZE = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
		
		ConnectionPool connectionPool = ConnectionPool.init(7);
		
		
		for (int i = 0; i < POOL_SIZE; i++) {
			Future<String> future =	executorService.submit(() -> {
				MockConnection connection = connectionPool.getConnection();
				String data = connection.getData();
				connectionPool.releaseConnection(connection);	
				return data;
			});
			
			while (!future.isDone()) {
				Thread.sleep(1000);
			}
			
			System.out.println(future.get());
		}
		
		executorService.shutdown();
	}
}
