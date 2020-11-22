package battleship;

public class EmptySea extends Ship {
	
	/**
	 * The length of the empty space
	 */
	private static final int length = 1;
	
	/**
	 * The type of ship (in this case, an empty ship)
	 */
	private static final String shipType = "empty";
	
	/**
	 * The String that should be used to denote hitting an empty space in Ocean's print method
	 */
	private static final String missMarker = "-";
	
	/**
	 * Calls the superclass Ship constructor, and sets the length to match the specific subclass length
	 */
	public EmptySea() {
		super(length);
	}
	
	/**
	 * Overrides shootAt(int row, int column) that is inherited from Ship. 
	 * Always returns false to indicate that nothing was hit.
	 * @return false
	 */
	@Override
	boolean shootAt(int row, int column) {
		return false;
	}
	
	/**
	 * Overrides isSunk()that is inherited from Ship.
	 * Always returns false to indicate that you didn’t sink anything.
	 * @return false
	 */
	@Override 
	boolean isSunk() {
		return false;
	}
	
	/**
	 * Returns the single-character “-“ String to use in the Ocean’s print method. 
	 * This is the character to be displayed if a shot has been fired, but nothing has been hit.
	 * @return String "-"
	 */
	@Override
	public String toString() {
		return missMarker;
	}
	
	/**
	 *Overrides abstract method in superclass
	 *Returns the ship type (in this case, the String "empty")  
	 *@return String shipType ("empty")
	 */
	@Override
	public String getShipType() {
		return shipType;
	}
}
