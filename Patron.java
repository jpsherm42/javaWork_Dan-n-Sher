package lms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

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
	
	/**
	 * Total amount in fines patron owes.
	 * (There is only a getter method for this variable; changing the value will be conducted by updating the database directly.)
	 * [[It is assumed in this case that a user and a librarian are not using the program at the same time,
	 * so once a patron's fine variable has been set, it will not/cannot change while the patron is using the program.]]
	 */
	private double fine;
	
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
		
		// construct SELECT query
		String getPatronInfoQuery = "SELECT firstName, lastName, numBooksOut, numHolds, totalFineAmount FROM patrons WHERE patron_ID = " + id + ";";
		
		String[] columns = {"firstName", "lastName", "numBooksOut", "numHolds", "totalFineAmount"};
		// pass to db and get results back
		String[][] patronInfo = DatabaseQueries.readFromDatabase(connection, getPatronInfoQuery, columns);		// returns 2 x 5 array; results are in row index 1
		
		if (patronInfo == null) {	// id passed does not exist in the db or some other error occured
			JOptionPane.showMessageDialog(null,"Invalid entry! Patron ID " + id + " does not exist.\nPlease try again or for a new user, create a new profile.", "Error!", JOptionPane.INFORMATION_MESSAGE);
			return;	// exit constructor
		}
		
		// valid/existing Patron; instantiate variables from 2nd row (index [1]) of array
		this.id = id;
		this.firstName = patronInfo[1][0];
		this.lastName = patronInfo[1][1];
		this.numBooksOut = Integer.parseInt(patronInfo[1][2]);	// convert to integer
		this.numHolds = Integer.parseInt(patronInfo[1][3]);
		this.fine = Double.parseDouble(patronInfo[1][4]);
		
	}
	
	// STATIC DATABASE METHODS
	/**
	 * Adds patron to database using first and last name; all other values are default and added by db itself.
	 * @param connection: Connection (Object) to use for database
	 * @param firstName: of patron
	 * @param lastName: of patron
	 */
	public static void createPatron(Connection connection, String firstName, String lastName) {
		DatabaseQueries.addToTable(connection, "patron", new String[]{firstName, lastName});		
	}
	
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
		
		String[] returnColumns = {"ID", "Title", "Author", "Available"};
		
		// if no search criteria passed, display all books (call that method)
		if (keyword == null || keyword == "" || criterion == null || criterion == "") {
			getAllBooks(connection, table, returnColumns);
			return;
		}
		
		// construct query string
		String searchQuery = "SELECT book_ID, title, author, NOT checkedOut AS Available "
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
		String allBooksQuery = "SELECT book_ID, title, author, NOT checkedOut AS Available "
				+ "FROM " + table
				+ " ORDER BY title, author;";
		//System.out.println(allBooksQuery);			// in case you want to print it to see it
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.readFromDatabase(connection, allBooksQuery, returnColumns);
		
	}
	
	// INSTANCE DATABASE METHODS
	/**
	 * Get a patron's holds or check-outs (determined by value of String `view`) by performing a SELECT query on the database (read, display results).
	 * Must be called on a Patron object in order to access the patron's specific ID.
	 * @param connection: Connection (Object) to use for database
	 * @param view:  Name of view to be queried
	 */
	public void getPatronBooks(Connection connection, String view) {
		
		// declare/initialize variables with useless values for now
		String selectQuery = "";
		String[] returnColumns = null;
		
		// conditions for which view table since we want to display different columns
		if (view.equals("holdsview")) {
			selectQuery = "SELECT book_ID, title, author "
					+ "FROM " + view
					+ " WHERE patron_ID = " + this.id 
					+ " SORT BY title;";
			//System.out.println(selectQuery);			// in case you want to print it to see it
			
			// give value/meaning to array
			returnColumns = new String[]{"Book ID", "Title", "Author"};
			
		} else {		// checkoutsview
			selectQuery = "SELECT book_ID, title, author, dueDate, daysLate AS `Days Overdue`, fineAmount AS Fine "
					+ "FROM " + view
					+ " WHERE patron_ID = " + this.id 
					+ " SORT BY dueDate, title;";
			//System.out.println(selectQuery);			// in case you want to print it to see it
			
			// give value/meaning to array
			returnColumns = new String[]{"Book ID", "Title", "Author", "Due Date", "Days Overdue", "Fine"};
		}
		
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.readFromDatabase(connection, selectQuery, returnColumns);
		
	}

	/**
	 * Get a list of books not on hold by Patron by performing a SELECT query on the database (read, display results).
	 * Must be called on a Patron object in order to access the patron's specific ID.
	 * @param connection: Connection (Object) to use for database
	 */
	public void getUnheldBooks(Connection connection) {
		
		// declare/initialize variables with useless values for now
		String selectQuery = "";
		
		// First uses a subquery to find all holds of this specific patron in holdsview.
		// Then join that selection with the books table, preserving all book rows
		selectQuery = "SELECT books.book_ID , books.title, books.author, books.genre, NOT books.checkedOut AS Available "
				+ "FROM (SELECT * FROM holdsview WHERE patron_ID = " + this.id + ") AS holds "
				+ "RIGHT JOIN books "
				+ "ON holds.book_ID = books.book_ID "
				+ "WHERE patron_ID IS NULL "
				+ "ORDER BY title, author;";
		
		String[] returnColumns = {"Book ID", "Title", "Author", "Genre", "Available"};
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, selectQuery, returnColumns);
		
	}
	
	//// ** be mindful when passing date that it's passed as a string in the form : YYYY-MM-DD
	// TODO: javadoc
	public void placeHold(Connection connection, int book_ID) {
		// TODO
		//(call changeTrue) -- depending on how selection of book is made, may need to check that a book exists BEFORE passing to method
	}
	
	//TODO: javadoc
	public void removeHold(Connection connection, int book_ID) {
		// TODO
		// (call changeFalse); notes:?: if book ID is NOT in holds table -> i think that means remove the book id/patron id pair from hold first before updating patron info	
	}
	
	//TODO: javadoc
	public void checkOutBook(Connection connection, int book_ID) {
		// TODO
		// check out book (call changeTrue) -- see notes for place hold!
	}
	
	//TODO: javadoc
	public void returnBook(Connection connection, int book_ID) {
		// TODO
		// return book (call changeFalse): search criterion is bookID
	}
	
	// TODO: javadoc
	public void changeBookStatusTrue(Connection connection, String table, int book_ID) {
		// TODO
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
	
	// TODO: javadoc
	public void changeBookStatusFalse(Connection connection, String table, int book_ID) {
		// TODO
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
	
	// SETTER METHODS
	/**
	 * Increases a patron's number of holds (variable) by 1.
	 * Updates value in database (calls updateColumn method) 
	 */
	public void incNumHolds(Connection connection) {
		this.numHolds += 1;
		DatabaseQueries.updateColumn(connection, "patron", "numHolds", Integer.toString(this.numHolds), "patron_ID", this.id);
	}
	
	/**
	 * Decreases a patron's number of holds (variable) by 1.
	 * Updates value in database (calls updateColumn method) 
	 */
	public void decNumHolds(Connection connection) {
		this.numHolds -= 1;
		DatabaseQueries.updateColumn(connection, "patron", "numHolds", Integer.toString(this.numHolds), "patron_ID", this.id);
	}
	
	/**
	 * Increases a patron's number of books out (variable) by 1.
	 * Updates value in database (calls updateColumn method) 
	 */
	public void incNumBooksOut(Connection connection) {
		this.numBooksOut += 1;
		DatabaseQueries.updateColumn(connection, "patron", "numBooksOut", Integer.toString(this.numBooksOut), "patron_ID", this.id);
	}
	
	/**
	 * Decreases a patron's number of books out (variable) by 1.
	 * Updates value in database (calls updateColumn method) 
	 */
	public void decNumBooksOut(Connection connection) {
		this.numBooksOut -= 1;
		DatabaseQueries.updateColumn(connection, "patron", "numBooksOut", Integer.toString(this.numBooksOut), "patron_ID", this.id);
	}
	
	// GETTER METHODS
	/**
	 * @return patron's ID (for specific Patron object)
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * @return first name (for specific Patron object)
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * @return patron's last name (for specific Patron object)
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * Method that probably won't get used, but nice to have just in case.
	 * @return patron's full name (for specific Patron object)
	 */
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
	
	/**
	 * @return fine amount (for specific Patron object)
	 */
	public double getFine() {
		return this.fine;
	}
	
	/**
	 * @return number of books on hold (for specific Patron object)
	 */
	public int getNumHolds() {
		return this.numHolds;
	}
	
	/**
	 * @return number of books checked out (for specific Patron object)
	 */	
	public int getNumBooksOut() {
		return this.numBooksOut;
	}
	
	/**
	 * @return max number of books a patron can check out
	 */
	public int getMaxBooks() {
		return Patron.MAX_BOOKS;
	}
	
	/**
	 * @return max number of books a patron can place on hold
	 */
	public int getMaxHolds() {
		return Patron.MAX_HOLDS;
	}
	
}