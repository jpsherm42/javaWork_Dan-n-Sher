package lms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for Patron objects; primarily for accessing the database and using the program as a Patron.
 * This serves as a Class more than an object; organizes functions specific to patrons (vs. to librarians).
 * Constructor adds new patrons to db.
 *
 */
public class Patron {
	
	// INSTANCE VARIABLES
	/**
	 * Patron's unique ID number stored as a String since this value will not change.
	 * (Database construction sets the minimum ID value at 10.)
	 */
	private String id;
	
	/**
	 * Patron's first name.
	 */
	private String firstName;
	/**
	 * Patron's last name.
	 */
	private String lastName;
	
	/**
	 * The number of books that a patron has on hold.
	 */
	private int numHolds;
	
	/**
	 * The number of books that a patron has checked out.
	 */
	private int numBooksOut;
	
	//static variables
	
	/**
	 * The max number of books a patron can have checked out.
	 * For the case of this project, all patrons are given the same max number.
	 */
	private static int MAX_BOOKS = 10;
	
	/**
	 * The max number of books a patron can have on hold.
	 * For the case of this project, all patrons are given the same max number.
	 */
	private static int MAX_HOLDS = 25;
	
	// CONSTRUCTOR
	/**
	 * Creates Patron (Java) Object that corresponds to a single row in the `patrons` table of the database.
	 * Instantiates instance variables from results of db query, which allows for Java code to easily/quickly access information about a patron
	 * (instead of continually calling on the database for a single value).
	 * @param connection: Connection (Object) to use for database
	 * @param id: integer value as a String corresponding to the unique identifier for a patron.
	 */
	public Patron(Connection connection, String id){
		this.id = id;
		
		// construct SELECT query
		String getPatronInfoQuery = "SELECT firstName, lastName, numBooksOut, numHolds, totalFineAmount FROM patrons WHERE patron_ID = " + id + ";";
		
		String[] columns = {"firstName", "lastName", "numBooksOut", "numHolds", "totalFineAmount"};
		// pass to db and get results back
		String[][] patronInfo = DatabaseQueries.readFromDatabase(connection, getPatronInfoQuery, columns);
		
		
	}
	
	// THIS ADDS IT TO THE DATABASE//calls constructor to instantiate variables
	public static void createPatron(Connection connection) {
		// update/write
		//add patron using patron.firstName, patron.whatever
		// if patron doesn't exist (first and last name don't exist), make one
				//addPatron(connection, table, this); // make sure we set the instance variables first
				// ** call addPatron() (which adds to the database )
				//this.id = 	// ** auto populates ID then let it do it's job and then ask the database for it
				// alternate : max(ID's) + 1
		
	}
	
	/*
	 * *************************************************************************
	 * FUNCTION TO CIRCLE BACK TO:
	 * Get a recommended book
				*** select query
	 *************************************************************************/
	
	/**
	 * Constructs SELECT query for `books` table.
	 * Calls functions to read from database based on query and display results.
	 * If keyword and/or criterion is null or an empty string (""), calls getAllBooks method instead.
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to be queried (in this case it should always be `books`)
	 * @param keyword: Name of column (`title`, `author`, or `genre`) (based on user input) to apply criterion to
	 * @param criterion: Search input from user
	 */
	public static void bookSearch(Connection connection, String table, String keyword, String criterion) {
		
		String[] returnColumns = {"Title", "Author", "Available"};
		
		// if no search criteria passed ==> patron wants to see all books
		if (keyword == null || keyword == "" || criterion == null || criterion == "") {
			getAllBooks(connection, table, returnColumns);
			return;
		}
		
		// construct query string
		String searchQuery = "SELECT title AS Title, author AS Author, NOT checkedOut AS Available "
				+ "FROM " + table + " "
				+ "WHERE " + keyword + " = \"" + criterion + "\" "
						+ "ORDER BY title, author;";
		//System.out.println(searchQuery);			// in case you want to print it to see it
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.readFromDatabase(connection, searchQuery, returnColumns);
		
	}
	
	/**
	 * Constructs SELECT query for `books` table to get all books in table.
	 * Calls functions to read from database based on query and display results.
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to be queried (in this case it should always be `books`)
	 * @param returnColumns: Array of Strings (column headers) corresponding to the columns that will be printed
	 */
	private static void getAllBooks(Connection connection, String table, String[] returnColumns) {
		// construct query string
		String allBooksQuery = "SELECT title AS Title, author AS Author, NOT checkedOut AS Available "
				+ "FROM " + table
				+ " ORDER BY title, author;";
		//System.out.println(allBooksQuery);			// in case you want to print it to see it
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.readFromDatabase(connection, allBooksQuery, returnColumns);
		
	}

	/**
	 * Get a patron's holds or check-outs (determined by value of String `view`) by performing a SELECT query on the database (read, display results).
	 * Must be called on a Patron object in order to access the patron's specific ID.
	 * @param connection: Connection (Object) to use for database
	 * @param view:  Name of view to be queried
	 */
	public void getPatronBooks(Connection connection, String view) {
		
		// declare/intialize variables with useless values for now
		String selectQuery = "";
		String[] returnColumns = null;
		
		// conditions for which view table since we want to display different columns
		if (view.equals("holdsview")) {
			selectQuery = "SELECT title AS Books "
					+ "FROM " + view + " "
					+ "WHERE patron_ID = " + this.id 
					+ " SORT BY title;";
			//System.out.println(selectQuery);			// in case you want to print it to see it
			
			// give value/meaning to array
			returnColumns = new String[]{"Books"};
		} else {		// checkoutsview
			selectQuery = "SELECT title AS Books, dueDate AS Due, daysLate AS `Days Overdue`, fineAmount AS Fine "
					+ "FROM " + view + " "
					+ "WHERE patron_ID = " + this.id 
					+ " SORT BY dueDate, title;";
			//System.out.println(selectQuery);			// in case you want to print it to see it
			
			// give value/meaning to array
			returnColumns = new String[]{"Books", "Due", "Days Overdue", "Fine"};
		}
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.readFromDatabase(connection, selectQuery, returnColumns);
		
	}
	
	
	/**
	 * Get a list of books not on hold by Patron by performing a SELECT query on the database (read, display results).
	 * Must be called on a Patron object in order to access the patron's specific ID.
	 * @param connection: Connection (Object) to use for database
	 * @param view:  Name of view to be queried (should always be holdsview)
	 * @param view:  Name of table to join on (should always be books)
	 */
	public void getUnheldBooks(Connection connection, String view, String table) {
		
		// declare/intialize variables with useless values for now
		String selectQuery = "";
		String[] returnColumns = {"Title", "Author", "Genre", "Available"};
		
		// First uses a subquery to find all holds of this specific patron in holdsview.
		// Then joins that selection with the books table, preserving all book rows
		selectQuery = "SELECT book.title AS title, book.author AS author, book.genre AS genre, NOT book.checkedOut AS Available"
				+ " FROM (SELECT * FROM " + view
					+ " WHERE patron_ID = " + this.id + ") AS holds"
				+ " RIGHT JOIN " + table + "AS books"
				+ " ON holds.book_ID = books.book_ID"
				+ " WHERE holds.patron_ID != " + this.id
				+ " SORT BY title, author;";
	
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, selectQuery, returnColumns);
		
	}
	
	
	
	public void changeBookStatusTrue(Connection connection, String table, int book_ID) {
		// used when placing a hold or check a book out
		// this method will call call addToTable on holds/checkouts and then updateTable on books (status) and on patrons (numbers)
		
		// UPDATE/write query
		
		// pass connx, query to write method
		// if table = holds, then colum = on_hold? 
		// if talbe = checkedout, then column  = checked_out?
		//UPDATE books.book_ID, books.column = true
		
		//UPDATE TABLE -- check to make sure that book_ID, this.ID tuple doesnt already exist
		// add to table: book_ID, this.ID, timedate.now()
	
	}
	
	public void changeBookStatusFalse(Connection connection, String table, int book_ID) {
		// used when removing hold or returning book
		// this method will call removeFromTable on Holds/checkouts and then updateTable on books (status) and on patrons (numbers)
		
		// UPDATE/write query
		
		// pass connx, query to write method
		// if table = holds, then colum = on_hold? 
		// if talbe = checkedout, then column  = checked_out?
		//UPDATE books.book_ID, books.column = False
		
		//UPDATE TABLE
		// REMOVE FROM table: book_ID, this.ID (there's only going to be one instance of this tuple)s
		
	}
	
	/**
	 ************************** MORE METHODS! ******************************
	 */
	
	// METHOD:  check out book (call changeTrue) -- see notes for place hold!
	// METHOD:  return book (call changeFalse)
	// METHOD:  place hold (call changeTrue) -- depending on how selection of book is made, may need to check that a book exists BEFORE passing to method
	// since this will be called from a patron, we KNOW that the patron exists
	// METHOD:  remove hold (call changeFalse) 
	
	// ** be mindful when passing date that it's passed as a string in the form : YYYY-MM-DD
	
}
