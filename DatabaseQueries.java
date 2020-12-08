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
	 * We assume that the column values will always be in the correct/same order every time that this method is called. The values array will always have more than 1 value; in fact it will always have at least 4 (lowest number of columns in db).
	 * All column names are used/considered, so default values will have to be passed in values array as appropriate/applicable.
	 * Creates a general/generic "INSERT INTO" query String with "?"'s equal to the number of columns (values.length). Each table has it's own case for creating the PreparedStatement with actual values/information/data.
	 * @param connection: Connection (Object) to use for database
	 * @param table: Name of table to add to
	 * @param values: Values for each column of table
	 */
	// 
	// 
	// 
	public static void addToTable(Connection connection, String table, String[] values) {		
		
		String insertQuery = "INSERT INTO " + table + " VALUES (?";
		
		// add appropriate number of ?'s
		// the ? above represents the 0th term, so we only need terms 1, ..., length-1
		for (int i = 1; i < values.length; i++) {
			insertQuery += ",?";
		}
		
		insertQuery += ")";
		
		//System.out.println(insertQuery);
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			// because all the tables are different and not all Strings, each one needs a separate add section and we can't use easy for-loops :(
			// yay for hard-coding!
			// INSERT VALUES INTO PreparedStatement
			
			if (table.equals("books")) {	// values will have length 8
				  /* first 6 columns (all passed as Strings)
				   * 1/0 book_ID int auto-increment -> pass the word **default**
				   * 2/1 title varchar(225)
				   * 3/2 author varchar(225)
				   * 4/3 genre varchar(225)
				   * 5/4 checkedOut tinyint(1) [BOOLEAN] -> pass the word **default**
				   * 6/5 onHold tinyint(1) [BOOLEAN] -> pass the word **default**
				   */
				  for (int i = 0; i <= 5; i++) {
					  preparedStatement.setString(i+1, values[i]);
				  }
				  
				  // 7th column (6th index): loanLength int
				  preparedStatement.setInt(7, Integer.parseInt(values[6]));
				  
				  // 8th column (7th index): dailyFineAmount float
				  preparedStatement.setFloat(8, Float.parseFloat(values[7]));
				  
			} else if (table.equals("patrons")) { // values will have length 8
				  /*
				  1/0 patron_ID int AI PK -> pass **default**
				  2/1 firstName varchar(225) 
				  3/2 lastName varchar(225) 
				  4/3 numBooksOut int  -> pass **default**
				  * 5/4 maxBooks int ==> pass as int
				  6/5 numHolds int  -> pass **default**
				  * 7/6 maxHolds int  ==> pass as int
				  8/7 totalFineAmount float -> pass **default**
				  */

				  // quickly fill values for Strings
				  int[] stringIndeces = {0,1,2,3,5,7};
				  
				  for (int index : stringIndeces) {
					  preparedStatement.setString(index + 1, values[index]);
				  }
				  
				  // 5/4 maxBooks int ==> pass as int
				  preparedStatement.setInt(5, Integer.parseInt(values[4]));
				  
				  // 7/6 maxHolds int  ==> pass as int
				  preparedStatement.setInt(7, Integer.parseInt(values[6]));
				  
			} else {	// table = "holds" or table = "checkouts"
				/*
			  	1/0 chkO_ID int AI PK -or- hold_ID int AI PK -> pass **default**
				2/1 book_ID int 
				3/2 patron_ID int 
				4/3 checkOutDate date	==> ** in the method that creates values, take current date/time and convert to string
			   */
			  
			  preparedStatement.setString(1, values[0]);
			  preparedStatement.setInt(2, Integer.parseInt(values[1]));
			  preparedStatement.setInt(3, Integer.parseInt(values[2]));

			  /*
			   * Got the below code from:
			   * https://stackoverflow.com/questions/23047200/convert-string-to-java-sql-date
			   * (not documentation)
			   */
			  SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
			  java.sql.Date sqlDate = null;
			  try {
				  java.util.Date utilDate = format.parse(values[3]);
			      sqlDate = new java.sql.Date(utilDate.getTime());
			    } catch (ParseException e) {
			        e.printStackTrace();
			    } 
			  preparedStatement.setDate(4, sqlDate);
			}
			
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
	 * @param keyword: Name column to apply criterion to / use for condition (almost always will be `book_ID` or `patron_ID`)
	 * @param criterion: Column value for desired row (input from user)
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
	 * @param keyword: Name of column to apply criterion to / use for condition (almost always will be `book_ID` or `patron_ID`)
	 * @param criterion: Column value for desired row (input from user)
	 */
	public static void updateColumn(Connection connection, String table, String column, String value, String keyword, String criterion) {
		String changeValQuery = "UPDATE " + table + " SET " + column + " = " + value + " WHERE " + keyword + " = " + criterion + ";";
		//System.out.println(changeValQuery);
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(changeValQuery);
			int rowsAffected = writeToDatabase(preparedStatement);
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
		Connection conn = DatabaseConnection.openDatabase();
		//Patron danTheMan = new Patron(conn, "patrons", "Dan", "Gallagher");
		String table = "books";
		String[] values = {"firstName", "LastName", "etc"};
		String column = "onHold";
		String value = "true";
		String keyword = "book_ID";
		String criterion = "10";
		//updateColumn(conn, table, column, value, keyword, criterion);
	}	
}