package gameLogic.interfaces;

import gameLogic.Entity;

/**
 * Interface used to define and object as Drawable
 *
 * @author Alex Dent
 *
 */
public interface Drawable {
	/**
	 * Returns the x position to draw the object
	 *
	 * @return int x
	 */
	public int getViewX();

	/**
	 * Returns the y position to draw the object
	 *
	 * @return int y
	 */
	public int getViewY();

	/**
	 * Returns the image filename string for the object
	 *
	 * @return String filename
	 */
	public String getImage();

	/**
	 * Returns the name of the object
	 *
	 * @return String name
	 */
	public String getName();

	/**
	 * Returns the description of the object
	 *
	 * @return String description
	 */
	public String getDescription();

	/**
	 * Returns the entity that is this object
	 *
	 * @return Entity
	 */
	public Entity getEntity();

	public boolean equals(Drawable d);
}
