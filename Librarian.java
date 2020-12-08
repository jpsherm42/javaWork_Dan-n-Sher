package lms;

import java.sql.Connection;

public class Librarian {
	/*ADDITIONAL FUNCTION
	 * UPDATE PATRON FINE (Manual input value)
	 */
	

	// just a place to organize/store our functions
	
	// no instance variables
	// no static variables
	// no constructors
	
	public static void addBook(Connection connection,  String table, String title, String author, String genre) {
		// UPDATE/write
		// add book
		
	}
	
	public static void removeBook(Connection connection,  String table, int bookID) {
		// UPDATE/write
		// add book
		
	}
	
	public static void removePatron(Connection connection,  String table, int patronID) {
		// update/write
		//remove patronID
	}
	
	// *** 12/7 made a change here to the parameters
	public static void getHoldsList(Connection connection, String holdsView, String keyword, String criterion) {
		//SELECT (Whateverwesaid) where keyword = criterion
		// default values are for getting all holds or checkouts (not based on a search criterion)
		// if keyword is default, then use a Select * 
		// otherwise
		
		/*
		 LIST HOLDS view: (we want all columns)
		 Title | Book ID | Patron | Patron ID | Request Date
		 */
		
		// 12/07 addition
		// *** SORT by Patron then Author then Title ****
		
	}
	
	public static void getCheckOutsList(Connection connection, String bookTable, String patronTable, String checkOutTable, String checkOutsView, String keyword, String criterion) {
		// keyword and criterion allow for filtering by title, author, patron
	
		/* *** view--> built in database by double join; select query of view
		 LIST CHECK-OUTS view:
		 1 patron_ID, 2firstName, 3lastName, 4book_ID, 5title, 6onHold, 7checkOutDate, 8dailyFineAmount, 9dueDate, 10daysLate, 11fineAmount
		 
		 desiredColumns = {1,2,3,4,5,6,7,9,10,11}
		 
		 
		// SO, this method may actually end up being the same as the getOverdue where we end up passing the keyword and criterion
		// and for overdues, the keyword is overdues or whwatever the name of the view is; and the criterion is ""/null
		 
		 ^^ when we ask for check outs or overdue, we'll just pass the column numbers we want [hard coded] (see below)
		 
		 
		 
		 */
	}
	
	public static void getOverdueList(Connection connection, String checkOutsView) {	// special case filtering of above based on date???
		// SELECT from VIEW
		// goal: list of over due books
		/* display the following columns from the CheckOutsList view
		 * where daysLate > 0
		 * desiredColums = all but 6 desiredColumns = {1,2,3,4,5,7,9,10,11}
		 * *** unless we group by patron_ID and just get the total that each patron owes?
		*/
	}
	
}
