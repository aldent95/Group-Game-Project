package gameLogic.interfaces;

import gameLogic.Tile;

import java.awt.Point;

/**
 * Interface to give the ability to find rooms to Player class
 *
 * @author Alex Dent
 *
 */
public interface roomFinder {
	/**
	 * Method to get the room at point p in the game map
	 *
	 * @param p
	 *            The point coord of the room to retrive
	 * @return The tile at point p
	 */
	public Tile getRoom(Point p);
}
