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
	
	public static void getList(Connection connection, String table, String keyword, String criterion) {	// set default values for keyword and criterion
		// default values are for getting all holds or checkouts (not based on a search criterion)
		// if keyword is default, then use a Select * 
		// otherwise
		
		//SELECT (Whateverwesaid) where keyword.criterion
	}
	
	// DAN
	public static void getOverdue(Connection connection, String bookTable, String patronTable, String checkOutTable) {
			
		// SELECT
		
		// goal: list of over due books (with patron name, if possible)
		
		// *** view--> built in database? by double join
		// booksID, title, author, check-out date, patron_ID, patron name, book.loanLength, calculated_column: due date (check-out + loanLength), daily fine
		
		// from the above view, select (title, atuhor, patron name (id's for both?), due date, calcedCol: daysLate = today-due date, calc_Col2: fineamt = dailyfine*daysLate
		// where daysLate > 0
	}
	
}
