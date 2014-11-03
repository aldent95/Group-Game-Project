package gameLogic.interfaces;

/**
 * Interface used to define an object as a key
 *
 * @author Alex Dent
 *
 */
public interface Key {
	/**
	 * Returns the Openable that this key
	 *
	 * @return Openable which the key opens
	 */
	public Openable getOpens();

	/**
	 * Sets the object this key opens
	 *
	 * @param e
	 *            Openable to open with this key
	 */
	public void setOpens(Openable e);
}
