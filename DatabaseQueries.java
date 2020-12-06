package lms;
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
	public static void readFromDatabase(Connection connection, String query) {
		
		try {
			
			String selectQuery = query;
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			//execute query and get result set
			ResultSet resultSet = preparedStatement.executeQuery();
			
			// this will eventually print to a WINDOW
			DatabaseQueries.printResultSet(resultSet);
			
			
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException SQLe) {
			SQLe.printStackTrace();
		}
	}
	
	/**
	 * Prints given ResultSet
	 * @param resultSet - ResultSet (Object) to print
	 */
	// JUSTIN
	private static void printResultSet(ResultSet resultSet) {
		
		//SPECIFIC TO BOOKS TABLE ******************************************************************************************************************
		System.out.println("book_ID\t| title\t| author\t| genre\t| Checked Out?\t| On Hold?\t| Loan Length (days)\t| Daily Fine Amount ($)");
		try {
			while (resultSet.next()) {
				// Get the column values via columnNumber
				int book_ID = resultSet.getInt(1);
				String title = resultSet.getString(2);
				String author = resultSet.getString(3);
				String genre = resultSet.getString(4);
				boolean checkedOut = resultSet.getBoolean(5);
				boolean onHold = resultSet.getBoolean(6);
				int loanLength = resultSet.getInt(7);
				int dailyFineAmount = resultSet.getInt(8);
				
				System.out.println(book_ID + "\t|\t" + title + "\t|\t" + author + "\t|\t" + genre + "\t|\t" + checkedOut + "\t|\t" + onHold + "\t|\t" + loanLength + "\t|\t" + dailyFineAmount);
			}
		} catch (SQLException SQLe) {
				SQLe.printStackTrace();
		}
	}
}
