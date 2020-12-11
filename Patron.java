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
	
	/**
	 * Places a book on hold for the specific patron (the user). First checks that the book is not already on hold by the patron requesting the hold.
	 * If so, gives warning. Otherwise, updates the patron's number of books on hold in Java and the database;
	 * adds a row to the `holds` table and updates the book's `onHold` column to reflect true (regardless of what the value was before the update);
	 * shows user "success" message/window.
	 * @param connection: Connection (Object) to use for database
	 * @param book_ID: String representation of book_ID in question
	 */
	public void placeHold(Connection connection, String book_ID) {	
		// get book title
		String title = DatabaseQueries.getBookTitle(connection, book_ID);
		
		// check if book_ID/patron_ID tuple already exists in database
		if (DatabaseQueries.bookPatronPairExists(connection, "holds", book_ID, this.id)) {
			// display error message/window
			JOptionPane.showMessageDialog(null, "You have already placed " + title + " (ID: " + book_ID + ") on hold.\nIf you want to place a book on hold, select \"Hold: Place\" on main patron page and enter a different ID.", "BOOK ALREADY ON-HOLD", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// else...
		// update patron's info in Java and db
		this.numHolds += 1;
		DatabaseQueries.updateColumn(connection, "patron", "numHolds", Integer.toString(this.numHolds), "patron_ID", this.id);

		// make date string to pass
		String dateAsString = DatabaseQueries.getTodaysDateAsString();
		String[] values = {book_ID, this.id, dateAsString};
		
		// add row to holds table
		DatabaseQueries.addToTable(connection, "holds", values);
		// update book's row in books table
		DatabaseQueries.updateColumn(connection, "books", "onHold", "true", "book_ID", book_ID);
		
		// display message
		JOptionPane.showMessageDialog(null, title + " (ID: " + book_ID + ") placed on hold!\nIf this was a mistake, select \"Hold: Remove\" on main patron page and enter this book's ID again.\nIf you want to place another book on hold, select \"Hold: Place\" on main patron page again and enter another book ID.", "BOOK PLACED ON-HOLD", JOptionPane.INFORMATION_MESSAGE);
			
	}
	
	/**
	 * Checks out a book to a specific patron (the user). First checks that the book is not already checked out by the patron requesting it.
	 * If so, gives warning. Otherwise, updates the patron's number of books checked out in Java and the database;
	 * adds a row to the `checkouts` table and updates the book's `checkedOut` column to reflect true (regardless of what the value was before the update);
	 * shows user "success" message/window.
	 * @param connection: Connection (Object) to use for database
	 * @param book_ID: String representation of book_ID in question
	 */
	public void checkOutBook(Connection connection, String book_ID) {
		// get book title
		String title = DatabaseQueries.getBookTitle(connection, book_ID);
		
		// check if book_ID/patron_ID tuple already exists in database
		if (DatabaseQueries.bookPatronPairExists(connection, "checkouts", book_ID, this.id)) {
			// display error message/window
			JOptionPane.showMessageDialog(null, "You've already checked out " + title + " (ID: " + book_ID + ").\nIf you want to check out a book, select \"Check Out Book\" on main patron page again and enter a different ID.", "BOOK ALREADY CHECKED-OUT", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// else...
		// update patron's info in Java and db
		this.numBooksOut += 1;
		DatabaseQueries.updateColumn(connection, "patron", "numBooksOut", Integer.toString(this.numBooksOut), "patron_ID", this.id);

		// make date string to pass
		String dateAsString = DatabaseQueries.getTodaysDateAsString();
		String[] values = {book_ID, this.id, dateAsString};
		
		// add row to holds table
		DatabaseQueries.addToTable(connection, "checkouts", values);
		// update book's row in books table
		DatabaseQueries.updateColumn(connection, "books", "checkedOut", "true", "book_ID", book_ID);
		
		// display message
		JOptionPane.showMessageDialog(null, title + " (ID: " + book_ID + ") checked out!\nIf this was a mistake, select \"Check In (Return) Book\" on main patron page and enter this book's ID again.\nIf you want to check out another book, select \"Check Out Book\" on main patron page again and enter another book ID.", "BOOK CHECKED-OUT", JOptionPane.INFORMATION_MESSAGE);
	}
		
	/**
	 * Removes a hold on a book for a specific patron (the user). Verifies that the book-patron pair exists in the `holds` table.
	 * If so, decrease's patron's numHolds in Java and database; removes row from `holds` table.
	 * Checks whether there are other holds on the book in question; if NOT, changes book's `onHold` column to false.
	 * Gives message to user.
	 * If the book-patron pair does not exist, gives user message.
	 * @param connection: Connection (Object) to use for database
	 * @param book_ID: String representation of book_ID in question
	 */
	public void removeHold(Connection connection, String book_ID) {
		// get book title
		String title = DatabaseQueries.getBookTitle(connection, book_ID);
		
		// verify that the book-patron pair exists in the holds table
		if (DatabaseQueries.bookPatronPairExists(connection, "holds", book_ID, this.id)) { // if so...
			// update patron's info in Java and db
			this.numHolds -= 1;
			DatabaseQueries.updateColumn(connection, "patron", "numHolds", Integer.toString(this.numHolds), "patron_ID", this.id);
			
			// get hold ID based on book-patron pair
			String hold_ID = DatabaseQueries.getID(connection, "holds", book_ID, this.id);
			
			// remove row from holds table
			DatabaseQueries.removeFromTable(connection, "holds", "hold_ID", hold_ID);
			
			// update book's row in books table IF the book ID does not show up in the holds table any more (since multiple patrons can place holds on the same book)
			if (!DatabaseQueries.checkForBook(connection, "holds", book_ID)) {	// only take action if this returns false (book NOT in table), so use !
				DatabaseQueries.updateColumn(connection, "books", "onHold", "false", "book_ID", book_ID);
				// print msg to console (just for the sake of this program for full transparency)
				System.out.println("ADDITION TO LIBRARIAN LOG: " + DatabaseQueries.getTodaysDateAsString() + " No more holds on " + title + " (ID: " + book_ID + ").");
			}
			
			// display message
			JOptionPane.showMessageDialog(null, "Removed hold on " + title + " (ID: " + book_ID + ").\nIf this was in error, select \"Hold: Place\" on main patron page and enter this book's ID again.\nIf you want to remove a hold on another book, select \"Hold: Remove\" on main patron page again and enter another book ID.", "BOOK HOLD REMOVED", JOptionPane.INFORMATION_MESSAGE);		
		} else {
			// give message
			JOptionPane.showMessageDialog(null, "You have not placed a hold on " + title + " (ID: " + book_ID + ") on hold.\nIf you want to place it on hold, select \"Hold: Place\" on the main patron page and enter the ID again.", "BOOK NOT ON-HOLD", JOptionPane.INFORMATION_MESSAGE);			
		}
		
	}
	
	/**
	 * Returns a book check out by a specific patron (the user). Verifies that the book-patron pair exists in the `checkouts` table.
	 * If so, decrease's patron's numBooksOut in Java and database; removes row from `checkouts` table.
	 * Changes book's `checkedOut` column to false.
	 * Gives message to user.
	 * If the book-patron pair does not exist, gives user message.
	 * @param connection: Connection (Object) to use for database
	 * @param book_ID: String representation of book_ID in question
	 */
	public void returnBook(Connection connection, String book_ID) {
		// get book title
		String title = DatabaseQueries.getBookTitle(connection, book_ID);
		
		// verify that the book-patron pair exists in the holds table
		if (DatabaseQueries.bookPatronPairExists(connection, "checkouts", book_ID, this.id)) { // if so...
			// update patron's info in Java and db
			this.numBooksOut -= 1;
			DatabaseQueries.updateColumn(connection, "patron", "numBooksOut", Integer.toString(this.numBooksOut), "patron_ID", this.id);
			
			// get chkO_ID based on book-patron pair
			String checkOut_ID = DatabaseQueries.getID(connection, "checkouts", book_ID, this.id);
			
			// remove row from checkouts table
			DatabaseQueries.removeFromTable(connection, "checkouts", "chkO_ID", checkOut_ID);
			
			// update book's row in books table
			DatabaseQueries.updateColumn(connection, "books", "checkedOut", "false", "book_ID", book_ID);
			
			// display message
			JOptionPane.showMessageDialog(null, title + " (ID: " + book_ID + ") returned.\nIf this was in error, select \"Check Out Book\" on the main patron page and enter this book's ID again.\nIf you want to return another book, select \"Check In (Return) Book\" on main patron page again and enter another book ID.", "BOOK RETURNED", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			// give message
			JOptionPane.showMessageDialog(null, "You have not checked out " + title + " (ID: " + book_ID + ").\nIf you want check it out, select \"Check Out Book\" on the main patron page and enter this book's ID again.", "BOOK NOT CHECKED-OUT", JOptionPane.INFORMATION_MESSAGE);			
		}
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