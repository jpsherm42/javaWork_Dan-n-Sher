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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class LibraryManagementSystem {
	
	// static variable available to entire class
	private static String selectedOption;
	
	public static void main(String[] args) {	
		// open db connection
		Connection connection = DatabaseConnection.openDatabase();
		runPatron(connection);
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
		selectedOption = "Search for Book"; // default
		// from https://www.javatpoint.com/java-actionlistener
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
			    //prints option right now       
				System.out.println(selectedOption);//tf.setText("Welcome to Javatpoint.");  
				// use this space to print all of the switch//if-else statements!
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
