package lms;

import java.sql.Connection;

public class Patron {
	//  for accessing and using the program and making a new patron

	//instance variables
	int id;
	String firstName;
	String lastName;
	
	//static variables
	static int MAX_BOOKS = 10;
	static int MAX_HOLDS = 25;
	
	//constructor (instantiates the instance variables by getting info from database)
	public Patron(Connection connection, String table, String firstName, String lastName){
		// get all the info from the patron table based on the passed name
		
		// if patron doesn't exist (first and last name don't exist), make one
		addPatron(connection, table, this); // make sure we set the instance variables first
		// ** call addPatron() (which adds to the database )
		//this.id = 	// ** auto populates ID then let it do it's job and then ask the database for it
		// alternate : max(ID's) + 1
		
	}
	
	// reconsider access modifier
	// THIS ADDS IT TO THE DATABASE
	private static void addPatron(Connection connection, String table, Patron patron) {
		// update/write
		//add patron using patron.firstName, patron.whatever
		
	}
	
	/* FUNCTION TO CIRCLE BACK TO:
	 * Get a recommended book
				*** select query
	 */
	
	public static void bookSearch(Connection connection,  String table, String keyWord, String criterion) {
		// SELECT/read query
		
		/* KEYWORD/CRITERION
		 * title
		 * author
		 * genre
		 */
		
		//pass connection and query string to readFromDatabase(connection, query)
		// SELECT * from books WHERE books.keyWord = criterion
		// we don't need all columns necessarily - loan length, daily fine amount; on hold
		// show if it's available or not (!checked_out column)
		
	}
	
	public void getPatronBooks(Connection connection, String table) {
		// SELECT/read query
		
		// pass connx, query string to read method
		// ***** title/author/avail   GOLD STANDARD
		// maybe add loan length?
			
		// SELECT (^^^) FROM
		// join of table and books ON Table.book_id = books.ID
		//WHERE patron_id = this.id
		
	}
	
	public void changeBookStatusTrue(Connection connection, String table, int book_ID) {
		// UPDATE/write query
		
		// pass connx, query to write method
		// if table = holds, then colum = on_hold? 
		// if talbe = checkedout, then column  = checked_out?
		//UPDATE books.book_ID, books.column = true
		
		//UPDATE TABLE -- check to make sure that book_ID, this.ID tuple doesnt already exist
		// add to table: book_ID, this.ID, timedate.now()
	}
	
	public void changeBookStatusFalse(Connection connection, String table, int book_ID) {
		// UPDATE/write query
		
		// pass connx, query to write method
		// if table = holds, then colum = on_hold? 
		// if talbe = checkedout, then column  = checked_out?
		//UPDATE books.book_ID, books.column = False
		
		//UPDATE TABLE
		// REMOVE FROM table: book_ID, this.ID (there's only going to be one instance of this tuple)s
	}
	
}
