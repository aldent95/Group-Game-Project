package networking.interfaces;

import gameLogic.Entity;
import gameLogic.InteractException;
import networking.TransmitTile;

/**
 * @author Michael Pearson <zl2mjp@gmail.com>
 * @version 0.1
 * @since 2014-09-18
 * Game Input interface
 * <p>This is the interface that will handle bindings between the game input & the networking partition of the game</p>
 */
public interface GameInputAcceptor {
	/**
	 *  moveForward indicates that the player should move forward.
	 */
	public void moveForward();
	/**
	 * moveBackwards indicate the player should move backwards
	 */
	public void moveBackwards();
	/**
	 * will instruct the player to pickup an item if possiable
	 * @return
	 * @throws InteractException
	 */
	public TransmitTile pickupItem(Entity e) throws InteractException;
	/**
	 * will instruct the player to drop an item from their inventory
	 */
	public TransmitTile dropItem(Entity e, Integer x, Integer y);
	/**
	 * Will change the viewers view by rotating to the left
	 */
	public void lookLeft();
	/**
	 * Will change the viewers view by rotating to the right
	 */
	public void lookRight();
	/**
	 * will disconnect the current player from the instance of the game. If the class implimenting this interface is the server it will remove the player. If it is the client
	 * it will send the disconnect message & the disconnect.
	 */
	public void disconnect();

	/**
	 * This will broadcast a message to all clients on the server
	 * @param message
	 */
	public void sendMessage(String message);

}
