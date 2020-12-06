package lms;

// imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages database queries.
 * @author sherm
 *
 */
public class DatabaseQueries {
	
	/**
	 * Query database
	 * @param connection - Connection (Object) to use
	 */
	public static void readFromDatabase(Connection connection, String selectQuery, int[] desiredColumns) {
		
		try {

	        //Run query
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// allows pointer to move forward and backward in the query; does not allow for updates
			// https://docs.oracle.com/javase/6/docs/api/java/sql/ResultSet.html
			
			//execute query and get result set
			ResultSet resultSet = preparedStatement.executeQuery();
			
			DatabaseQueries.printResultSet(resultSet, desiredColumns);
			
			resultSet.close();
			preparedStatement.close();
			
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
	}
	
	/**
	 * Prints given ResultSet into window format
	 * only given columns
	 * @param resultSet - ResultSet (Object) to print
	 */
	private static void printResultSet(ResultSet resultSet, int[] desiredColumns) {
				
		try {
			
			// when there are results returned
			if(resultSet!=null) {
				
				//point at last entry/rows
				resultSet.last();
				// get row value
				int numRows = resultSet.getRow();
				// point back to before the first entry/row
			    resultSet.beforeFirst();
			    
				
		        //Create the appropriately sized array for your results
		    	String[][] results = new String[numRows][desiredColumns.length];
		    	
		    	
		    	
				//Put results in array
		        for(int row = 0; row < numRows; row++){
		        	
		        	int col = 0;
		        	
		        	if(resultSet.next()){	// if there are results to read/point at
		        		// Get the column values via columnNumber
			        	// only use the specified columns
		        		for(int column: desiredColumns){
		        			
		        			if (column == 5) {	// check-out column
		        				// availability is opposite of checkedOut
		        				Boolean available = !resultSet.getBoolean(column);
		        				
		        				if (available) {
		        					results[row][col] = "Available";
		        				} else {
		        					results[row][col] = "Not Available";
		        				}
		        				
		        			} else {
		        				results[row][col] = resultSet.getString(column);
		        			}
		        			
			       			col++;
			       		}
		        	}
		        	
		        }
							
		     // ***************** CHANGE THIS TO A MESSAGE WINDOW *******
		        for (int r = 0; r < results.length; r++) {
					for (int c = 0; c < results[r].length; c++) {
						System.out.print(results[r][c] + "__");
					}
					System.out.print("\n");
				}
		        
				
			} else {		// not results returned
				// ***************** CHANGE THIS TO A MESSAGE WINDOW *******
				System.out.println("There are no results to display");
			}
			
		} catch (SQLException SQLe) {
				SQLe.printStackTrace();
		}
		
		
		//SPECIFIC TO BOOKS TABLE ******************************************************************************************************************
		//System.out.println("book_ID\t| title\t| author\t| genre\t| Checked Out?\t| On Hold?\t| Loan Length (days)\t| Daily Fine Amount ($)");
		/*
		
		int book_ID = resultSet.getInt(1);
		String title = resultSet.getString(2);
		String author = resultSet.getString(3);
		String genre = resultSet.getString(4);
		boolean checkedOut = resultSet.getBoolean(5);
		boolean onHold = resultSet.getBoolean(6);
		int loanLength = resultSet.getInt(7);
		int dailyFineAmount = resultSet.getInt(8);
		
		System.out.println(book_ID + "\t|\t" + title + "\t|\t" + author + "\t|\t" + genre + "\t|\t" + checkedOut + "\t|\t" + onHold + "\t|\t" + loanLength + "\t|\t" + dailyFineAmount);
		*/
	}
	
	public static void main(String[] args) {
		
		// open db connection
		Connection connection = DatabaseConnection.openDatabase();
		
		//boolean usingDB = true;
		
		String table = "books";	//books2
		String query = "SELECT * FROM " + table;
		int[] desiredColumns = {2,3,5};	// title, author, checkedOut
		
		DatabaseQueries.readFromDatabase(connection, query, desiredColumns);		
		
	}
	
}


/*
Statement s = connection.createStatement();
//Determine table size (max number of results that can be generated)
s.execute("SELECT COUNT(*) FROM " + table);
ResultSet rows = s.getResultSet();
rows.next(); //point at RS
int numRows = Integer.parseInt(rows.getString(1));
*/