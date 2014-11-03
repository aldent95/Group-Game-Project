package gameLogic.interfaces;

import gameLogic.Item;

import java.util.ArrayList;

/**
 * Interface used to define an object as a container
 *
 * @author Alex Dent
 *
 */
public interface Container {

	/**
	 * Gets the contents of the container
	 *
	 * @return returns the contents of the container in an ArrayList<Item>
	 */
	public ArrayList<Item> getContents();

	/**
	 * Sets the contents of the container
	 *
	 * @param items
	 *            The ArrayList<Items> to set the container to
	 */
	public void setContents(ArrayList<Item> items);

	/**
	 * Add item i to the container
	 *
	 * @param i
	 *            item to add
	 */
	public void addContents(Item i);

	/**
	 * Remove item I from the container
	 *
	 * @param i
	 *            item to remove
	 * @return returns true if successful, false otherwise
	 */
	public boolean removeContents(Item i);
}
