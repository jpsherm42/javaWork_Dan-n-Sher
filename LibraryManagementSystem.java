package lms;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class LibraryManagementSystem {
	
	// ("global") static variables available to entire class (including anonymous classes)
	private static String selectedOption = "Search for Book"; // default value
	private static Connection connection;
	private static Patron patron;
	
	public static void main(String[] args) {	
		// open db connection
		connection = DatabaseConnection.openDatabase();
		runPatron(connection);
		
		
		// close database
		DatabaseConnection.closeDatabase(connection);
	}
	
	
	public static void runPatron(Connection connection) {
		JFrame patronWindow = new JFrame();
		
		JPanel patronContents = new JPanel();
		//patronContents.setBackground(Color.WHITE);
		GridLayout pageLayout = new GridLayout(4,1,10,0);
		patronContents.setLayout(pageLayout);
		
		JLabel header = new JLabel("Patron Main Page");
		header.setFont(new Font("Arial", Font.BOLD, 15));
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setForeground(Color.black);
		patronContents.add(header);
		
		JLabel instructions = new JLabel("Select an Action...");
		instructions.setHorizontalAlignment(SwingConstants.CENTER);
		patronContents.add(instructions);
		
		// create options
		String[] options = {"Search for Book",
				"My Books on Hold",
				"My Books Checked Out",
				"Get Book Book Recommendation",
				"Show My Fine",
				"Hold: Place",
				"Hold: Remove",
				"Check Out Book",
				"Check In (Return) Book"};
		
		JComboBox patronDropDown = new JComboBox(options);
		patronDropDown.setSelectedIndex(0);
		
		// from https://www.javatpoint.com/java-actionlistener -> anonymous class
		patronDropDown.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				// set value of selectedOption
				// https://stackoverflow.com/questions/4962416/preferred-way-of-getting-the-selected-item-of-a-jcombobox
				selectedOption = String.valueOf(patronDropDown.getSelectedItem());  
		}  
		});
		patronContents.add(patronDropDown);
		
		Button goButton = new Button("GO!");
		goButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
			    //prints option selected       
				//System.out.println(selectedOption);//tf.setText("Welcome to Javatpoint.");  

				// actions based on selection
				switch (selectedOption) {				
					case "Search for Book":
					    // TODO
						//WINDOW, drop-down list (keyword = type of search), 1 entry field, search button -> call the printFromDatabase/printResultSetinWindow methods /  see all books button -> getAllBooks
					    break;
					case "My Books on Hold":
						// TODO
						// WINDOW of results ** add availability column to Patron method
						break;
					case "My Books Checked Out":
						// TODO
						// WINDOW of results ** with overdue status and fine columns in Patron method
						break;
					case "Get Book Book Recommendation":
						// TODO
						// console entry for genre; WINDOW of results (use bookSearch method)
						break;
					case "Show My Fine":
						// TODO
						// JOptionPane
					    break;
					case "Hold: Place":
						// TODO
						// make sure to check that the input value is correct before passing to the method use method in DatabaseQueries class
						
						// if user's numHolds = 25, they can't place another hold: display warning message
						if (patron.getNumHolds() == patron.getMaxHolds()) {
							JOptionPane.showMessageDialog(null, "You have already placed 25 books on hold.\nYou may not place any additional holds without removing one or more holds first.", "MAX HOLD REACHED", JOptionPane.INFORMATION_MESSAGE);
						} else {
							//WINDOW with list of books NOT on hold (call user.getUnheldBooks); console prompt & entry for book id (then call place hold method)//(read as string!)
						}
					    break;
					case "Hold: Remove":
						// TODO
						// make sure to check that the input value is correct before passing to the method use method in DatabaseQueries class
						// [this is just a call of the regular holdsview and setting patron_ID to this.id] WINDOW with list of books on hold; console prompt & entry for book id (read as string!)
					    break;
					case "Check Out Book":
						// TODO
						// make sure to check that the input value is correct before passing to the method use method in DatabaseQueries class
						
						// if user's numBooksOut = 10; they can't check out: display warning message
						if (patron.getNumBooksOut() == patron.getMaxBooks()) {
							JOptionPane.showMessageDialog(null, "You have already checked out 10 books.\nYou may not check out any more until you return at least 1 book. Happy reading!", "MAX CHECK-OUTS REACHED", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							// WINDOW with list of books NOT checked out by anyone (select title/author from books where checkedOut = 0); console entry for book id//(read as string!)
						}
					    break;
					case "Check In (Return) Book":
						// TODO
						// make sure to check that the input value is correct before passing to the method use method in DatabaseQueries class
						// call getMyCheckouts, console entry for book id//(read as string!)
					    break;
				}
				
			  }  
			});
		
		JPanel bottomContents = new JPanel();
		bottomContents.setLayout(new GridLayout(1,5));
		bottomContents.add(new JLabel()); // place holder
		bottomContents.add(new JLabel()); // place holder
		bottomContents.add(goButton);
		bottomContents.add(new JLabel()); // place holder
		bottomContents.add(new JLabel()); // place holder
		patronContents.add(bottomContents);
		
		patronWindow.add(patronContents);
		
		// call method to display Welcome Window
		//patronWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		//patronWindow.setPreferredSize(new Dimension(400, 300));
		patronWindow.pack();		
		patronWindow.setLocationRelativeTo(null);
		patronWindow.setTitle("Patrons - Main Page");
		patronWindow.setVisible(true);
		
		
		
	}
	
}
