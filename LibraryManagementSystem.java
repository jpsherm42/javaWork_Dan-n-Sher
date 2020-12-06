package lms;

import java.sql.Connection;

public class LibraryManagementSystem {
	
	public static void main(String[] args) {
		
		// open db connection
		Connection connection = DatabaseConnection.openDatabase();
		
		//boolean usingDB = true;
		
		//DatabaseQueries.readFromDatabase(connection, "SELECT * FROM books");		
		
	}
	
	// search for first and last name

	/*
	 * Patron functions:
			Search for books based on input criteria
				select queries
			Place a book on hold/remove a hold
				update
			Check a book in/out
				update query
			Get list of books on hold
				select query
			Get list of books checked out
				select query
			Get a recommended book
				select query
						
		Librarian functions:
			Add book to database
				update query
			Add patron to database
				call the Patron constructor; update query
			Get list of holds by patron, by book, or all holds
				select query
			Get list of checked out books
				select query
			Determine a book’s overdue status (based on computer’s datetime)
				select query
	 */
	
	
	/*
	 * determine which table keyword to pass
	 * get the book id from title/author - if this pair exists; error message? // for multiple copies, get min ID ??? 
	 * determine whether to place value as True or False
	 * 
	 * Lib: remove book -need to get book id (if pair exists, etc.)
	 * lib: add patron - FORM/inputs -> strings -> patron object (call constructor)
	 * lib: remove patron - get patron id (if pair exists, etc.)
	 * 
	 * 
	 * 
	 * FUNCTIOn: getting an iD based on tuple; if it doens't exist, give a nasty message
	 */
	
	
}
