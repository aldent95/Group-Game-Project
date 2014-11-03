package gameLogic;

import gameLogic.interfaces.Drawable;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import storage.SaveData;
import storage.Saveable;
/**
 * Tile class, used to define a Tile/room and the methods for using it
 * @author Alex Dent
 *
 */
public class Tile implements Serializable, Saveable {
	private static final long serialVersionUID = -8652986073658295290L;
	private HashMap<String, Wall> walls = new HashMap<String, Wall>();
	private ArrayList<Entity> objects = new ArrayList<Entity>();
	private int xsize;
	private int ysize;
	private String name;
	private String description;
	/**
	 * Tile constructor
	 * @param x - int, x size of the room
	 * @param y - int, y size of the room
	 * @param w - HashMap<String, Wall>, the walls of the room
	 * @param n - String, the name of the room
	 * @param des - String, the rooms description
	 */
	public Tile(int x, int y, HashMap<String, Wall> w, String n, String des) {
		xsize = x;
		ysize = y;
		if (w != null) {
			walls = w;
		}
		setName(n);
		setDescription(des);
	}

	protected ArrayList<Drawable> getView(String dir, int width, boolean first) {
		ArrayList<Drawable> temp = new ArrayList<Drawable>();
		int gridBlocksPixelSize = width / xsize;
		Entity tempEnt = null;
		int xmid = 0;
		int ymid = 0;
		if(first){
		xmid = xsize / 2;
		ymid = ysize / 2;
		}
		temp.add(walls.get(dir));
		switch (dir) {
		case "North":
			if(!first) ymid = ysize;
			for (int y = 0; y < ymid; y++) {
				for (int x = 10; x < xsize - 10; x++) {
					for (Entity e : objects) {
						if (e.getPos().y == y && e.getPos().x == x) {
							tempEnt = e;
							tempEnt.setViewX(tempEnt.getPos().x
									* gridBlocksPixelSize);
							temp.add(tempEnt);
						}
					}
				}
			}
			break;
		case "South":
			if(!first) ymid = 0;
			for (int y = ysize; y > ymid; y--) {
				for (int x = 10; x < xsize - 10; x++) {
					for (Entity e : objects) {
						if (e.getPos().y == y && e.getPos().x == x) {
							tempEnt = e;
							tempEnt.setViewX(tempEnt.getPos().x
									* gridBlocksPixelSize);
							temp.add(tempEnt);
						}
					}
				}
			}
			break;
		case "East":
			if(!first) xmid = 0;
			for (int x = xsize; x > xmid; x--) {
				for (int y = 10; y < ysize -10; y++) {
					for (Entity e : objects) {
						if (e.getPos().y == y && e.getPos().x == x) {
							tempEnt = e;
							tempEnt.setViewX(tempEnt.getPos().y
									* gridBlocksPixelSize);
							temp.add(tempEnt);
						}
					}
				}
			}
			break;
		case "West":
			if(!first) xmid = xsize;
			for (int x = 0; x < xmid; x++) {
				for (int y = 10; y < ysize - 10; y++) {
					for (Entity e : objects) {
						if (e.getPos().y == y && e.getPos().x == x) {
							tempEnt = e;
							tempEnt.setViewX(tempEnt.getPos().y
									* gridBlocksPixelSize);
							temp.add(tempEnt);
						}
					}
				}
			}
			break;
		}
		Collections.reverse(temp);
		return temp;
	}

	protected HashMap<String, Wall> getWalls() {
		return walls;
	}

	public void setObjects(ArrayList<Entity> objects) {
		this.objects = objects;
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
	/***
	 * Used to get the name of the room
	 * @return String
	 */
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	protected void addObject(Entity e) {
		objects.add(e);
	}

	protected boolean removeObject(Entity e) {
		return objects.remove(e);
	}

	public Wall getWall(String dir) {
		return walls.get(dir);
	}

	protected boolean hasEntity(Entity e) {
		if (objects.contains(e))
			return true;
		return false;
	}

	@Override
	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.addField("walls", walls);
		s.addField("objects", objects);
		s.addField("xsize", xsize);
		s.addField("ysize", ysize);
		s.addField("name", name);
		s.addField("description", description);
		return s;
	}

	@SuppressWarnings("unchecked")
	public Tile(SaveData s) {
		this.walls = (HashMap<String, Wall>) s.getField("walls");
		this.objects = (ArrayList<Entity>) s.getField("objects");
		this.xsize = (int) s.getField("xsize");
		this.ysize = (int) s.getField("ysize");
		this.name = (String) s.getField("name");
		this.description = (String) s.getField("description");
	}

	public boolean equals(Tile t) {
		if (!walls.equals(t.getWalls()))
			return false;
		if (!objects.equals(t.objects))
			return false;
		if (xsize != t.xsize)
			return false;
		if (ysize != t.ysize)
			return false;
		if (!name.equals(t.getName()))
			return false;
		if (!description.equals(t.getDescription()))
			return false;
		return true;
	}

	protected void setupEntity(Entity e, int x, int y, String dir) {
		switch (dir) {
		case "North":
			e.setPos(new Point(x, 1));
			break;
		case "South":
			e.setPos(new Point(x, ysize-1));
			break;
		case "East":
			e.setPos(new Point(xsize-1, x));
			break;
		case "West":
			e.setPos(new Point(1, x));
			break;

		}
	}

}
