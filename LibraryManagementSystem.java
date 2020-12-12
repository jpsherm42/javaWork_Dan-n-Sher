package lms;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryManagementSystem {
	
	
	/*
	public static void main(String[] args) {
		Connection connection = DatabaseConnection.openDatabase();
		
		String table = "patrons";
		String[] bookValues = {"fake title", "fake author", "fake genre"};
		String[] patronValues = {"fake first", "fake last"};
		String column = "checkedOut";
		String value = "false";
		String keyword = "patron_ID";
		String criterion = "11";
		
		String selectQuery = "SELECT title, author FROM books;";
		String[] columns = {"Title", "Author"};
		
		
		printFromDatabase(connection, selectQuery, columns);
		
		
		
		
	}*/
	public static void main(String[] args) {
		
		// open db connection
		Connection connection = DatabaseConnection.openDatabase();
		
		//Patron justinSherman = new Patron(connection, "101");
		
		/*
		System.out.println(justinSherman.getFine());
		System.out.println(justinSherman.getFirstName());
		System.out.println(justinSherman.getLastName());
		System.out.println(justinSherman.getFullName());
		System.out.println(justinSherman.getID());
		System.out.println(justinSherman.getMaxBooks());
		System.out.println(justinSherman.getMaxHolds());
		System.out.println(justinSherman.getNumBooksOut());
		System.out.println(justinSherman.getNumHolds());
		*/
		
		//Patron.createPatron(connection, "fakid", "testman");
		Patron fakidTestman = new Patron(connection, "11");
		
		String keyword = "";
		String criterion = "Brandon Krakowski";
		fakidTestman.returnBook(connection, "12");
		
		
	}
	
}
