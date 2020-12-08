package lms;

// imports
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

/**
 * Manages database queries.
 * @author sherm
 *
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
		        // ***************** CHANGE THIS TO A MESSAGE WINDOW *******
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
	 * Writes to a database using the passed PreparedStatement--executes update and then closes.
	 * Other methods construct the specific PreparedStatement necessary to perform the desired action.
	 * @param preparedStatement: PreparedStatement to execute
	 * @return rowsAffected:  The number of rows affected by the update.
	 */
	public static int writeToDatabase(PreparedStatement preparedStatement) {//Connection connection, String[] tables, String[] columns, String[] values) {
		int rowsAffected = 0;
		try {
			rowsAffected = preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
		
		return rowsAffected;
	}
	
}