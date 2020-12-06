package lms;

import java.sql.Connection;

public class Librarian {

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
	
	public static void getHoldsList(Connection connection, String bookTable, String patronTable, String checkOutTable, String holdsView, String criterion) {	// set default value and criterion
		//SELECT (Whateverwesaid) where keyword.criterion
		// default values are for getting all holds or checkouts (not based on a search criterion)
		// if keyword is default, then use a Select * 
		// otherwise
		
		// ************ we'll need views for the join between the 3 tables
		// SO, this method may actually end up being the same as the getOverdue where we end up passing the keyword and criterion
		// and for overdues, the keyword is overdues or whwatever the name of the view is; and the criterion is ""/null
		
		/*
		 
		 LIST HOLDS view: (we want all columns)
		 Title | Book ID | Patron | Patron ID | Request Date
		 
		 */
		
		
	}
	
	public static void getCheckOutsList(Connection connection, String bookTable, String patronTable, String checkOutTable, String checkOutsView, String keyword, String criterion) {
		// keyword and criterion allow for filtering by title, author, patron
	
		/* *** view--> built in database by double join; select query of view
		 LIST CHECK-OUTS view:
		 1 Title | 2 Book ID | 3 Patron | 4 Patron ID | 5 Check-out Date (*this one may not be necessary) | 6 Due Date = Check-out Date + books.loanLength | 7 daysLate = today-due date | 8 fineAmt = books.dailyfine * daysLate | 9 On Hold?
		 
		 
		 ^^ when we ask for check outs or overdue, we'll just pass the column numbers we want [hard coded] (see below)
		 
		 
		 
		 */
	}
	
	public static void getOverdueList(Connection connection, String checkOutsView) {	// special case filtering of above based on date
		// SELECT from VIEW
		// goal: list of over due books
		/* display the following columns from the CheckOutsList view
		 * where daysLate > 0
		 * desiredColums = all but 9
		*/
	}
	
}
