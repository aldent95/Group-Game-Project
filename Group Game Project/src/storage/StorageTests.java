/**
 * Storage System Tests
 *
 * @author Andrew Buntain - 300024338
 *
 * All of these tests work the same:
 *
 * 		1.	Make an object (possibly including many sub objects).
 * 		2.	Save the object into a file.
 * 		3.	Read an object out of the file.
 * 		4.	Compare the original object with the one read from the file.
 *
 * 		If the objects are the same, the test is a success.
 */

package storage;

import java.awt.Point;

import org.junit.Test;

import render.CreateGameWorld;
import gameLogic.GameLogic;
import gameLogic.Item;
import gameLogic.Player;
import gameLogic.Tile;
import gameLogic.Wall;

public class StorageTests {

	public static final String DEFAULT_TEST_FILENAME = "test.xml";

	// Just a player
	@Test
	public void testPlayer(){
		Player in = new Player("Andy", "This is an example player.", new Point(1,2), "player_image.png", 0, null);
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		Player out = (Player)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// Just an empty room
	@Test
	public void testTile(){
		Tile in = new Tile(1000,2000,null,"Example Room","This is an example of a room");
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		Tile out = (Tile)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// A blank wall
	@Test
	public void testWall(){
		Wall in = new Wall("Example Wall", "This is an example wall", "wall_image.png");
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		Wall out = (Wall)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// An item
	@Test
	public void testItem(){
		Item in = new Item("Example Item", "This is an example item", new Point(100,200), "item_image.png",0,true);
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		Item out = (Item)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// A player with an item in inventory
	@Test
	public void testPlayerWithInventory(){
		Player in = new Player("Andy", "This is an example player.", new Point(1,2), "player_image.png", 0, null);
		Item item1 = new Item("Example Item", "This is an example item", new Point(100,200), "item_image.png",0,true);
		in.addItem(item1);
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		Player out = (Player)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// A GameLogic object
	@Test
	public void testGameLogic(){
		GameLogic in = new GameLogic();
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		GameLogic out = (GameLogic)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// A GameLogic object with a player
	@Test
	public void testGameLogicWithPlayer(){
		GameLogic in = new GameLogic();
		Player p = new Player("Andy", "This is an example player.", new Point(1,2), "player_image.png", 0, null);
		in.addPlayer(p);
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		GameLogic out = (GameLogic)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}

	// The complete default game world
	@Test
	public void testFullGameWorld(){
		GameLogic in = CreateGameWorld.load();
		Saver.saveTestObject(in,DEFAULT_TEST_FILENAME);
		GameLogic out = (GameLogic)Loader.loadTestObject(DEFAULT_TEST_FILENAME);
		assert out.equals(in);
	}


}
