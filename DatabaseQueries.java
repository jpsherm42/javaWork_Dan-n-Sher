package lms;

// imports
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

/**
 * Manages database queries.
 */
public class DatabaseQueries {
	
	/**
	 * Perform SELECT query with connection to database
	 * @param connection: Connection (Object) to use for database
	 * @param selectQuery: String of SELECT query language
	 * @param columns: Array of Strings (column headers) corresponding to the columns that will be printed 
	 */
	public static void readFromDatabase(Connection connection, String selectQuery, String[] columns) {
		
		try {

	        //Run query
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// allows pointer to move forward and backward in the query; does not allow for updates
			// https://docs.oracle.com/javase/6/docs/api/java/sql/ResultSet.html
			
			//execute query and get result set
			ResultSet resultSet = preparedStatement.executeQuery();
			
			DatabaseQueries.printResultSet(resultSet, columns);
			
			resultSet.close();
			preparedStatement.close();
			
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
	}
	
	/*
	 * Prints given ResultSet in window format
	 * only given columns
	 * @param resultSet - ResultSet (Object) to print
	 * @param columns: Array of Strings (column headers) corresponding to the columns that will be printed
	 */
	private static void printResultSet(ResultSet resultSet, String[] columns) {
				
		try {
			//point at last entry/row
			resultSet.last();
			// row number (if there are none, this will be 0)
			int numRows = resultSet.getRow();
			
			// when there are results returned
			if(numRows != 0) {
				
				//point at last entry/rows
				resultSet.last();
				// get row value
				numRows = resultSet.getRow();
				// point back to before the first entry/row
			    resultSet.beforeFirst();
				
		        // Create the appropriately sized array for your results
			    // 1 extra row for column (field) headers
		    	String[][] results = new String[numRows+1][columns.length];
		    	
		    	// add column headers to 0th row of results array
		    	
		    	for (int cl = 0; cl < columns.length; cl++) {
		    		results[0][cl] = columns[cl];
		    	}
		    	
		    	
				//Put results in array
		        for(int row = 1; row <= numRows; row++){
		        	
		        	int col = 0;
		        	
		        	if(resultSet.next()){	// if there are results to read/point at
		        		// Get the column values via column headers (String value)
			        	// only use the specified column headers
		        		for(String column: columns){
		        			
		        			// AVAILABLE COLUMN
		        			if (column.equals("Available")) {
		        				// availability is opposite of checkedOut
		        				Boolean available = resultSet.getBoolean(column);
		        				if (available) {
		        					results[row][col] = "Available";
		        				} else {
		        					results[row][col] = "Not Available";
		        				}
		        			// FINE COLUMN: format to display $ and 2 decimal places
		        			} else if (column.equals("Fine")){
		        				double fineAmount = resultSet.getDouble(column);
		        				results[row][col] = "$" + String.format("%2.2f",fineAmount);		// ASSUMPTION: hopefully no one owes more than $ 99.75 in fines!
		        			
		        			// DUE (date) COLUMN:  format to display day of week and month (MMM) and day (DD)
		        			} else if (column.equals("Due")){
		        				Date dueDate = resultSet.getDate(column);
		        				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
		        				results[row][col] = dateFormat.format(dueDate);
		        			
		        			} else {
		        			
		        				results[row][col] = resultSet.getString(column);
		        			}
		        			
			       			col++;
			       		}
		        	}
		        }
							
		        // Class.displayInWindow(String[][] results)
		        // ***************** CHANGE THIS TO A MESSAGE WINDOW ******* *******************************************************************************************
		        for (int r = 0; r < results.length; r++) {
					for (int c = 0; c < results[r].length; c++) {
						System.out.print(results[r][c] + "__");
					}
					System.out.print("\n");
				}
		        
				
			} else {		// not results returned
				JOptionPane.showMessageDialog(null, "There are no entries to display for the given search criteria.", "Query Results", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch (SQLException SQLe) {
				SQLe.printStackTrace();
		}
		
	}
	
	/**
	 * Adds a row of values to the given table.
	 * We assume that the column values will always be in the correct/same order every time that this method is called.
	 * Each table has it's own case for creating the query (String) with actual values/information/data or default, as necessary/appropriate/applicable.
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to add to
	 * @param values: Values for each column of table
	 */
	public static void addToTable(Connection connection, String table, String[] values) {		
		
		String insertQuery = "INSERT INTO " + table + " VALUES (";
		
		// because all the tables are different and not all Strings, each one needs a separate add section and we can't use easy for-loops :(
		// yay for hard-coding! It also makes more sense to just construct a robust string vs. making a string with "?"'s and then updating the PreparedStatement
		// If the array values does not have the proper length for a given table; prints a message and exits the method
		
		if (table.equals("books")) {
			
			// we assume that for the purposes of this program, all books will get the default loan length and daily fine amount
			if (values.length != 3) {	// values array should have length 3 for title [0], author [1], and genre [2]
				JOptionPane.showMessageDialog(null, "Could not add row to table " + table + " due to improper number of entry values.", "Improper Entry", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			insertQuery += "default,\"" + values[0] 	// title 
					+ "\",\"" + values [1] 				// author
							+ "\",\"" + values[2] 		// genre
									+ "\",default,default,default,default);";
			  
		} else if (table.equals("patrons")) {
			
			// we assume that for the purposes of this program, all patrons will get the default maxBooksOut and maxHolds values
			if (values.length != 2 ){ // values array should have length 2 for first name [0] and last name [1]
				JOptionPane.showMessageDialog(null, "Could not add row to table " + table + " due to improper number of entry values.", "Improper Entry", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			insertQuery += "default,\"" + values[0] 	// first name
					+ "\",\"" + values[1]				// last name 
							+ "\",default,default,default,default,default);";
			  
		} else {	// table = "holds" or table = "checkouts"
			
			if (values.length != 3) {// values array should have length 3 for book_ID [0], patron_ID [1], date [2]
				JOptionPane.showMessageDialog(null, "Could not add row to table " + table + " due to improper number of entry values.", "Improper Entry", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			// This method is called for adding holds or checkouts from a patron, so we know the foreign key patron_ID will exist in the patrons table;
			// the code to ensure that the book_ID exists in the books table will occur prior to calling this method.
			// Therefore, we will assume that by this point/for this method, addition to the table will be possible.
			
			insertQuery += "default," + values[0]	// book_ID (in the table it's a number, so it is not necessary to pass quotations for it 
					+ "," + values[1]				// patron_ID
							+ ",\"" + values[2]		// date (as a string)
									+ "\");";
		}
		
		//System.out.println(insertQuery);
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			int rowsAffected = writeToDatabase(preparedStatement);
			JOptionPane.showMessageDialog(null, rowsAffected + " entry added to " + table, "Insert Results", JOptionPane.INFORMATION_MESSAGE);
			
		} catch (SQLException SQLe){
			SQLe.printStackTrace();
		}		
		
	}
	
	/**
	 * Remove a row/entry from the given table using the condition passed by `keyword` = criterion
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to remove from
	 * @param keyword: Name column to apply criterion to / use for condition (will always be `book_ID` or `patron_ID`)
	 * @param criterion: ID number of desired row (input from user) [***If for some reason the criterion is for a varchar column, the \" 's will have to be passed, too as the query constructor does NOT include those.]
	 */
	public static void removeFromTable(Connection connection, String table, String keyword, String criterion) {
		
		String removeQuery = "DELETE FROM " + table + " WHERE " + keyword + " = " + criterion + ";";
		//System.out.println(removeQuery);
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
			int rowsAffected = writeToDatabase(preparedStatement);
			
			JOptionPane.showMessageDialog(null, rowsAffected + " entry(ies) removed from " + table, "Removal Results", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}		
		
	}
	
	/**
	 * 
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to update
	 * @param column: Name column to update value for
	 * @param value: Value to change above column to
	 * @param keyword: Name of column to apply criterion to / use for condition (will always be `book_ID` or `patron_ID`)
	 * @param criterion: ID number of desired row (input from user) [***If for some reason the criterion is for a varchar column, the \" 's will have to be passed, too as the query constructor does NOT include those.]
	 */
	public static void updateColumn(Connection connection, String table, String column, String value, String keyword, String criterion) {
		String changeValQuery = "UPDATE " + table + " SET " + column + " = " + value + " WHERE " + keyword + " = " + criterion + ";";
		//System.out.println(changeValQuery);
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(changeValQuery);
			int rowsAffected = writeToDatabase(preparedStatement);	// will return 0 if no change made; no harm, no foul
			JOptionPane.showMessageDialog(null, rowsAffected + " entry updated in " + table, "Update Results", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
		
	}
	
	/**
	 * Writes to a database using the passed PreparedStatement--executes update and then closes.
	 * Other methods construct the specific PreparedStatement necessary to perform the desired action.
	 * @param preparedStatement: PreparedStatement to execute
	 * @return rowsAffected:  The number of rows affected by the update.
	 */
	public static int writeToDatabase(PreparedStatement preparedStatement) {
		int rowsAffected = 0;
		try {
			rowsAffected = preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
		
		return rowsAffected;
	}
	
	
	/*
	 * for testing purposes
	 */
	public static void main(String[] args) {
		Connection connection = DatabaseConnection.openDatabase();
		
		String table = "patrons";
		String[] bookValues = {"fake title", "fake author", "fake genre"};
		String[] patronValues = {"fake first", "fake last"};
		String column = "checkedOut";
		String value = "false";
		String keyword = "patron_ID";
		String criterion = "11";
		
		// ***** SAVE - CONVERT DATE TO STRING IN GIVEN FORMAT
		// FROM :  https://www.javatpoint.com/java-date-to-string
		java.util.Date date = java.util.Calendar.getInstance().getTime();  
		java.text.DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");  
		String strDate = dateFormat.format(date);
	}
	
}