package battleship;

public abstract class Ship {
	
	/**
	 * The row that contains the bow (front part of the ship)
	 */
	private int bowRow;
	
	/**
	 * The column that contains the bow (front part of the ship) 
	 */
	private int bowColumn;
	
	/**
	 * The length of the ship
	 */
	private int length;
	
	/**
	 * A boolean that represents whether the ship is going to be placed horizontally or vertically
	 * true is horizontal; false vertical 
	 */
	private boolean horizontal;
	
	/**
	 * An array of booleans that indicate whether that part of the ship has been hit or not
	 */
	private boolean[] hit;
	
	/**
	 * Constructor. sets the length property of the particular ship and initializes the hit array
	 * @param length
	 * @return 
	 */
	public Ship(int length) {
		this.length = length;
		//initialize hit array with 4 slots (Piazza @ 556)
		this.hit = new boolean[4];		// ********** Changed by justin ***********************************
	}
	
	//getters
	
	/**
	 * Returns the ship length 
	 * @return int ship length
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Returns the row corresponding to the position of the bow 
	 * @return the row corresponding to the position of the bow 
	 */
	public int getBowRow() {
		return this.bowRow;
	}
	
	/**
	 * Returns the bow column location 
	 * @return the bow column location 
	 */
	public int getBowColumn() {
		return this.bowColumn;
	}
	
	/**
	 * Returns the hit array 
	 * @return the hit array
	 */
	public boolean[] getHit() {
		return this.hit;
	}
	
	/**
	 * Returns whether the ship is horizontal or not 
	 * @return true if horizontal; otherwise false
	 */
	public boolean isHorizontal() {
		return this.horizontal;
	}
	
	//setters
	
	/**
	 * Sets the value of bowRow
	 * @param row
	 */
	public void setBowRow(int row) {
		this.bowRow = row;
	}
	
	/**
	 * Sets the value of bowColumn 
	 * @param column
	 */
	public void setBowColumn(int column) {
		this.bowColumn = column;
	}
	
	/**
	 * Sets the value of the instance variable horizontal 
	 * @param horizontal
	 */
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}
	
	//abstract methods
	
	/**
	 * Returns the type of ship as a String.  Every specific type of Ship (e.g. BattleShip,Cruiser, etc.) 
	 * has to override and implement this method and return the corresponding ship type. 
	 * @return
	 */
	public abstract String getShipType();
	
	//other methods
	
	/**
	 * Helper method that creates an array of coordinate pairs based on the given row, column, length, and orientation.
	 * For placement consistency, horizontal ships face East(bow at right end) and vertical
	 * ships face South(bow at bottom end).
	 * This means, if you place a horizontal battleship at location (9, 8) in the
	 * ocean, the bow is at location (9, 8) and the rest of the ship occupies locations: (9, 7), (9, 6), (9, 5).
	 * If you place a vertical cruiser at location (4, 0) in the ocean, the bow is at location (4, 0) and the rest 
	 * of the ship occupies locations: (3, 0), (2, 0).
	 * @param row
	 * @param column
	 * @param horizontal
	 * @return the array of coordinates
	 */
	int[][] allShipCoordinates(int row, int column, boolean horizontal){
		
		//NOTE: This would definitely be a lot cleaner if we can just import the JavaTuples object, but I wasn't sure if we could
		//so I went with this. Maybe we should ask?
		
		//create a 2d array with width of the ship length and length of 2 (representing row, column pairs)
		//since that is how many pairs there will be
		int[][] shipCoordinates = new int[this.length][2];
		
		//loop through the array to set the values
		for (int i = 0; i< shipCoordinates.length; i++) {
			for (int j = 0; j < shipCoordinates[i].length; j++) {
				
				// The coordinates are created by summing two terms, one of which will always be 0, based on the value of j
				// When j is 0, we want to assign the row value, so we multiply the row value by -1*(j-1), since this evaluates to
				// 1 when j = 0 and 0 when j = 1. Similarly, we multiply the column value by j, since j can only be 0 or 1.
				// Thus, when j is 0, the column term is 0, and when j is 1, the row term is 0.
				
				// if the ship is horizontal, the row will stay the same and the column will change
				// Thus, our column value decreases by 1 for each part of the ship
				if (horizontal) {
					shipCoordinates[i][j] = (row * -1 * (j-1)) + ((column - i) * j);
				}
				// if the ship is vertical, the row will change and the column will stay the same
				// Thus, our row value decreases by 1 for each part of the ship
				else {
					shipCoordinates[i][j] = ((row - i) * -1 * (j-1)) + (column * j);
				}
			}
		}
		
		return shipCoordinates;
	}
	
	/**
	 * Based on the given row, column, and orientation, returns true if it is okay to put a ship of this 
	 * length with its bow in this location; false otherwise. The ship must not overlap another ship, 
	 * or touch another ship (vertically, horizontally, or diagonally), and it must not”stick out” beyond the array. 
	 * Does not actually change either the ship or the Ocean -it just says if it is legal to do so. 
	 * @param row
	 * @param column
	 * @param horizontal
	 * @param ocean
	 * @return
	 */
	boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
		
		//get ship array in ocean
		Ship[][] shipArray = ocean.getShipArray();
		
		//get an array of all the coordinates this ship will take up
		int[][] shipCoordinates = allShipCoordinates(row, column, horizontal);
		
		//first, loop through ship coordinates to make sure none goes beyond the scope of the array
		//that is, if they are less than 0 or greater than the shipArray length
		//If any does, return false
		for (int i = 0; i< shipCoordinates.length; i++) {
			for (int j = 0; j < shipCoordinates[i].length; j++) {
				if (shipCoordinates[i][j] < 0 || shipCoordinates[i][j] > shipArray.length) {
					return false;
				}
			}
		}
		
		//loop through ship coordinates, and for each, check if there are any ship objects in the shipArray with type != "empty"
		//Only looping through the rows, since its easy to specifiy 0 or 1 for j when looping through the shipArray
		for (int i=0; i< shipCoordinates.length; i++) {
			
			//loop through ship array at and surrounding ship coordinates
			//starts at the current row value -1 and ends at that value + 1, doing the same for the corresponding
			//column value, thus checking the coordinate and each space around it, including diagonals
			//There's a bit of redundancy in this algorithm, but it should do for our purposes
			for (int m = shipCoordinates[i][0] - 1; m< shipCoordinates[i][0] +2; m++) {
				for (int n=shipCoordinates[i][1] - 1; n <shipCoordinates[i][1] + 2; n++) {
					
					// if the coordinate finds a shiptype that is not "empty", we cannot place our ship, so return false
					if (shipArray[m][n].getShipType() != "empty") {
						return false;
					}
				}					
			}
		}
		
		//If the ship passed the tests, return true
		return true;
	}
	
	/**
	 * “Puts” the ship in the ocean. This involves giving values to the bowRow, bowColumn, and horizontal instance 
	 * variables in the ship, and it also involves putting a reference to the ship in each of 1 or more locations 
	 * (up to 4) in the ships array in the Ocean object. (Note: This will be as many as four identical references; 
	 * you can’t refer to a ”part” of a ship, only to the whole ship.) For placement consistency (although it doesn’t 
	 * really affect how you play the game), horizontal ships face East(bow at right end) and vertical
	 * ships face South(bow at bottom end).
	 * This means, if you place a horizontal battleship at location (9, 8) in the
	 * ocean, the bow is at location (9, 8) and the rest of the ship occupies locations: (9, 7), (9, 6), (9, 5).
	 * If you place a vertical cruiser at location (4, 0) in the ocean, the bow is at location (4, 0) and the rest 
	 * of the ship occupies locations: (3, 0), (2, 0).
	 * @param row
	 * @param column
	 * @param horizontal
	 * @param ocean
	 */
	void placeShipAt(int row, int column, boolean horizontal, Ocean ocean) {
		
		//set ship instance variables
		this.bowRow = row;
		this.bowColumn = column;
		this.horizontal = horizontal;
		
		//place in Ocean object
		//get the shipArray from ocean
		Ship[][] shipArray = ocean.getShipArray();
		
		//get an array of all the coordinates this ship will take up
		int[][] shipCoordinates = allShipCoordinates(row, column, horizontal);
		
		//loop through ship coordinate pairs and place a reference in the shipArray at each corresponding coordinate
		for (int i = 0; i< shipCoordinates.length; i++) {
			//set this Ship instance at the corresponding coordinate pair in shipArray
			shipArray[shipCoordinates[i][0]][shipCoordinates[i][1]] = this;
			}
		
	}
	
	/**
	 * If a part of the ship occupies the given row and column, and the ship hasn’t been sunk, 
	 * mark that part of the ship as “hit” (in the hit array, index 0 indicates the bow) and return true; 
	 * otherwise return false.
	 * @param row
	 * @param column
	 * @return true if a non-sunk ship has been hit; otherwise return false
	 */
	boolean shootAt(int row, int column) {
		//if the ship is sunk, return false
		if (this.isSunk()) {
			return false;
		}
		
		//if one of the Ship's coordinate pairs equals the row, column pair, return true
		
		//get array of Ship's coordinates
		int[][] shipCoordinates = this.allShipCoordinates(this.bowRow, this.bowColumn, this.horizontal);
		
		for(int i=0; i<shipCoordinates.length; i++) {
			if((shipCoordinates[i][0] == row) && (shipCoordinates[i][1] == column)) {
				
				// update hit array
				if (this.isHorizontal()) {
					for (int k = 0; k < this.getLength(); k++) {
						if (column == this.getBowColumn() - k) {
							this.getHit()[k] = true;
						}
					}
					
				} else {		// vertical
					for (int n = 0; n < this.getLength(); n++) {
						if (row == this.getBowRow() - n) {
							this.getHit()[n] = true;
						}
					}
				}
				// return "outcome" of shoot
				return true;
			}
		}
		
		//if no match was found, return false
		return false;
	}
	
	/**
	 * Lets us know if all parts of the ship have been hit
	 * @return true if every part of the ship has been hit, false otherwise
	 */
	boolean isSunk() {
		
		int truesCounter = 0;
		// iterate through the hit array but only to the index corresponding to the length of the specific ship subclass
		for(int i = 0; i < this.getLength(); i++) {
			if (this.getHit()[i] == true) {
				truesCounter++;
			}
		}
		
		// same number of hits (trues) as the length, then the ship is sunk
		if (truesCounter == this.getLength()) {
			return true;
		} else {
			return false;
		}
		
		// DAN'S ORIG CODE ***********************************************************************
		//loops through hit array. If one of its slots has not been hit, return false, as the ship is not sunk
		//for (boolean part: this.hit) {
			//if (part == false) {
				//return false;
			//}
		//}
		
		//otherwise, return true
		//return true;
		// *****************************************************************************************
	}
	
	/**
	 * Returns a single-character String to use in the Ocean’s print method. 
	 * This method can be used to print out locations in the ocean that have been shot at; 
	 * it should not be used to print locations that have not been shot at. 
	 * Since toString behaves exactly the same for all ship types, it is placed here in the Shipclass.
	 * 
	 * @Return ”s” if the ship has been sunk and ”x” if it has not been sunk. 
	 */
	@Override
	public String toString() {
		
		if (this.isSunk()) {
			return "s";
		}
		else {
			return "x";
		}
	
	}
}