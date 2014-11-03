package gameLogic.interfaces;

/**
 * Interface used to define if an object is a treasure
 *
 * @author Alex Dent
 *
 */
public interface Treasure {
	/**
	 * Used to get the value of the treasure
	 *
	 * @return The int value of the treasure
	 */
	public int getValue();

	/**
	 * Used to set the value of the treasure
	 *
	 * @param i
	 *            int value to set the treasure to
	 */
	public void setValue(int i);
}
