package lms;

import java.sql.Connection;

public class LibraryManagementSystem {
	
	public static void main(String[] args) {
		
		// open db connection
		Connection connection = DatabaseConnection.openDatabase();
		
		//boolean usingDB = true;
		
		DatabaseQueries.readFromDatabase(connection);
		
		
	}

}
