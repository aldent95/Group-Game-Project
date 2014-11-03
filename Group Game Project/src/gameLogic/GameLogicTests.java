package gameLogic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gameLogic.interfaces.Drawable;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class GameLogicTests {
	GameLogic g;
	HashMap<String, Wall> walls;
	HashMap<String, Wall> walls2;
	Tile t1;
	Tile t2;
	Player p1;
	Player p2;
	Item i1;
	Item i2;
	Item i3;

	@Test
	public void getView1() {
		setup();
		p1.setDir("East");
		ArrayList<Drawable> temp = new ArrayList<Drawable>();
		p2.setPos(new Point(0, 0));
		temp.add(i1);

		temp.add(walls.get("East"));

		temp.add(i2);
		temp.add(i3);
		temp.add(walls2.get("East"));
		ArrayList<Drawable> temp2 = g.getView(10);
		if (temp.size() != temp2.size())
			fail("Size difference: " + temp.size() + " " + temp2.size());
		for (int i = 0; i < temp.size(); i++) {
			if (!(temp.get(i).equals(temp2.get(i))))
				fail("Object difference");
		}

	}

	@Test
	public void getView2() {
		setup();
		ArrayList<Drawable> temp = new ArrayList<Drawable>();
		temp.add(walls.get("North"));
		assertTrue(g.getView(10).equals(temp));
	}

	@Test
	public void playerEquals1() {
		setup();
		assertTrue(g.getPlayer().equals(p1));
	}

	@Test
	public void playerEquals2() {
		setup();
		assertTrue(p1.equals(p1));
	}

	@Test
	public void playerEquals3() {
		setup();
		assertFalse(p2.equals(p1));
	}
	@Test
	public void playerEquals4() {
		setup();
		try{
		assertFalse(p2.equals("Test"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	@Test
	public void playerEquals5() {
		setup();
		try{
		assertFalse(p2.equals(null));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void ItemEquals1() {
		setup();
		assertTrue(i1.equals(i1));
	}

	@Test
	public void ItemEquals2() {
		setup();
		assertFalse(i1.equals(i2));
	}
	@Test
	public void ItemEquals3() {
		setup();
		try{
			assertFalse(i1.equals("String"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	@Test
	public void ItemEquals4() {
		setup();
		try{
			assertFalse(i1.equals(null));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void RoomEquals1() {
		setup();
		assertTrue(t1.equals(t1));
	}

	@Test
	public void RoomEquals2() {
		setup();
		assertFalse(t1.equals(t2));
	}

	@Test
	public void WallEquals1() {
		setup();
		assertTrue(walls.get("North").equals(walls.get("North")));
	}

	@Test
	public void WallEquals2() {
		setup();
		assertFalse(walls.get("North").equals(walls.get("South")));
	}

	@Test
	public void WallEquals3() {
		setup();
		assertFalse(walls.get("North").equals(walls2.get("North")));
	}
	@Test
	public void addRoom1(){
		setup();
		Tile temp = new Tile(100, 100, walls, "test", "test");
		g.addRoom(0, 1, temp);
		assertTrue(temp.equals(g.getRoom(new Point(0,1))));
	}
	@Test
	public void addRoom2(){
		setup();
			assertFalse(g.addRoom(0, 0, null));
	}
	@Test
	public void newPlayer(){
		setup();
		Player temp1 = new Player("Test", "Test", new Point(0,0), "Test", 0, g);
		Player temp2 = g.newPlayer("Test", "Test", new Point(0,0), 0);
		assertTrue(temp1.equals(temp2));
	}
	@Test
	public void newPlayer2(){
		setup2();
		g.newPlayer("Test", "Test", new Point(0,0), 0);
		Player temp2 = new Player("Test1", "Test", new Point(0,0), "Test", 0, g);
		Player temp3 = g.newPlayer("Test", "Test", new Point(0,0), 0);
		assertTrue(temp2.equals(temp3));
	}
	@Test
	public void move1(){
		setup();
		p1.moveForward();
		assertTrue(p1.getDir().equals("North") && p1.getPos().equals(new Point(0,0)));
	}
	@Test
	public void move2(){
		setup();
		p1.lookLeft();
		assertTrue(p1.getDir().equals("West") && p1.getPos().equals(new Point(0,0)));
	}
	@Test
	public void move3(){
		setup();
		p1.lookRight();
		p1.moveForward();
		assertTrue(p1.getDir().equals("East") && p1.getPos().equals(new Point(1,0)));
	}
	@Test
	public void GameLogic1() {
		setup();
		assertTrue(g.equals(g));
	}
	@Test
	public void KeyEquals(){
		setup();
		IKey k1 = new IKey("Test", "Test", new Point(50,50), "Test", 0, true);
		k1.setOpens(g.getRoom(new Point(0,0)).getWall("North"));
		assertTrue(k1.equals(k1));
	}
	@Test
	public void KeyEquals2(){
		setup();
		IKey k1 = new IKey("Test", "Test", new Point(50,50), "Test", 0, true);
		IKey k2 = new IKey("Test", "Test", new Point(50,50), "Test", 0, true);
		k1.setOpens(g.getRoom(new Point(0,0)).getWall("North"));
		k2.setOpens(g.getRoom(new Point(0,0)).getWall("South"));
		assertFalse(k1.equals(k2));

	}

	private void setup() {
		g = new GameLogic(2, 2, true);
		walls = new HashMap<String, Wall>();
		walls.put("North", new Wall("Test", "North", "North file0"));
		walls.put("South", new Wall("Test", "South", "South file0"));
		walls.put("East", new Wall("Test", "East", "East file0"));
		walls.put("West", new Wall("Test", "West", "West file0"));
		t1 = new Tile(100, 100, walls, "Test", "Test");
		g.addRoom(0, 0, t1);
		walls2 = new HashMap<String, Wall>();
		walls2.put("North", new Wall("Test", "North", "North file1"));
		walls2.put("South", new Wall("Test", "South", "South file1"));
		walls2.put("East", new Wall("Test", "East", "East file1"));
		walls2.put("West", new Wall("Test", "West", "West file1"));
		t2 = new Tile(100, 100, walls2, "Test", "Test");
		g.addRoom(1, 0, t2);
		t2.getWall("West").setLeadsTo(t1);
		t1.getWall("East").setLeadsTo(t2);
		i1 = new Item("test", "Item 1", new Point(90, 25), "Item file 1", 0,
				true);
		i2 = new Item("test", "Item 2", new Point(20, 25), "Item file 2", 0,
				true);
		i3 = new Item("test", "Item 3", new Point(90, 25), "Item file 3", 0,
				false);
		t1.addObject(i1);
		t2.addObject(i2);
		t2.addObject(i3);
		p2 = g.newPlayer("P2", "P2", new Point(1, 0), 0);
		p1 = g.newPlayer("P1", "P1", new Point(0, 0), 0);

	}
	private void setup2() {
		g = new GameLogic(2, 2, true);
		walls = new HashMap<String, Wall>();
		walls.put("North", new Wall("Test", "North", "North file0"));
		walls.put("South", new Wall("Test", "South", "South file0"));
		walls.put("East", new Wall("Test", "East", "East file0"));
		walls.put("West", new Wall("Test", "West", "West file0"));
		t1 = new Tile(100, 100, walls, "Test", "Test");
		g.addRoom(0, 0, t1);
		walls2 = new HashMap<String, Wall>();
		walls2.put("North", new Wall("Test", "North", "North file1"));
		walls2.put("South", new Wall("Test", "South", "South file1"));
		walls2.put("East", new Wall("Test", "East", "East file1"));
		walls2.put("West", new Wall("Test", "West", "West file1"));
		t2 = new Tile(100, 100, walls2, "Test", "Test");
		g.addRoom(1, 0, t2);
		t2.getWall("West").setLeadsTo(t1);
		t1.getWall("East").setLeadsTo(t2);
		i1 = new Item("test", "Item 1", new Point(90, 25), "Item file 1", 0,
				true);
		i2 = new Item("test", "Item 2", new Point(20, 25), "Item file 2", 0,
				true);
		i3 = new Item("test", "Item 3", new Point(90, 25), "Item file 3", 0,
				false);
		t1.addObject(i1);
		t2.addObject(i2);
		t2.addObject(i3);
	}
}
