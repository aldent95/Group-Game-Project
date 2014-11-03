package gameLogic;

import gameLogic.interfaces.Key;
import gameLogic.interfaces.Openable;
import gameLogic.interfaces.roomFinder;

import java.awt.Point;
import java.util.ArrayList;

import networking.TransmitTile;
import networking.interfaces.GameInputAcceptor;
import storage.Linkable;
import storage.SaveData;
import storage.Saveable;

/**
 * Player class, used to define a player and methods for controling players
 *
 * @author Alex Dent
 *
 */
public class Player extends Character implements GameInputAcceptor, Saveable,
		Linkable {

	private static final long serialVersionUID = 8831598387434402743L;
	private ArrayList<Item> inventory = new ArrayList<Item>();
	private roomFinder finder;
	private boolean connected = true;
	// Error codes
	private static final int notInRoom = 0;
	private static final int notItem = 1;
	private static final int notPickup = 2;
	private static final int inventoryFull = 3;

	/**
	 * Player constructor
	 *
	 * @param name
	 *            - String, name of the player
	 * @param des
	 *            - String, description of the player
	 * @param pos
	 *            - Point, contains the coords of the room the player starts in
	 * @param fileName
	 *            - String, the filename string of the image for the player
	 * @param y
	 *            - int, the height at which the renderer draws the player image
	 * @param r
	 *            - roomFinder, used to find which room the player is in
	 */
	public Player(String name, String des, Point pos, String fileName, int y,
			roomFinder r) {
		super(name, des, pos, fileName, y);
		finder = r;
	}

	@Override
	protected void move(String dir) {
		if (dir.equals("Left") || dir.equals("Right"))
			rotate(dir);
		else if (dir.equals("Forward")) {
			if (!finder.getRoom(super.getPos()).getWall(super.getDir())
					.isSolid()
					&& (!finder.getRoom(super.getPos()).getWall(super.getDir())
							.isLocked() || canOpen(finder.getRoom(
							super.getPos()).getWall(super.getDir())))) {
				switch (super.getDir()) {
				case "North":
					super.setPos(new Point(super.getPos().x,
							super.getPos().y - 1));
					break;
				case "South":
					super.setPos(new Point(super.getPos().x,
							super.getPos().y + 1));
					break;
				case "East":
					super.setPos(new Point(super.getPos().x + 1,
							super.getPos().y));
					break;
				case "West":
					super.setPos(new Point(super.getPos().x - 1,
							super.getPos().y));
					break;

				}
			}
		} else if (dir.equals("Backward")) {
			String tempdir = "";
			switch (super.getDir()) {
			case "North":
				tempdir = "South";
				break;
			case "South":
				tempdir = "North";
				break;
			case "East":
				tempdir = "West";
				break;
			case "West":
				tempdir = "East";
				break;
			}
			if (!finder.getRoom(super.getPos()).getWall(tempdir).isSolid()
					&& (!finder.getRoom(super.getPos()).getWall(tempdir)
							.isLocked() || canOpen(finder.getRoom(
							super.getPos()).getWall(tempdir)))) {
				switch (tempdir) {
				case "North":
					super.setPos(new Point(super.getPos().x,
							super.getPos().y - 1));
					break;
				case "South":
					super.setPos(new Point(super.getPos().x,
							super.getPos().y + 1));
					break;
				case "East":
					super.setPos(new Point(super.getPos().x + 1,
							super.getPos().y));
					break;
				case "West":
					super.setPos(new Point(super.getPos().x - 1,
							super.getPos().y));
					break;

				}
			}
		}
	}

	private boolean canOpen(Openable wall) {
		for (Item i : getInventory()) {
			if (i instanceof Key) {
				if (((Key) i).getOpens().equals(wall))
					if(wall instanceof Wall){
						((Wall) wall).setLocked(false);
					}
					return true;
			}
		}
		return false;
	}

	private void rotate(String dir) {
		if (dir.equals("Left")) {
			switch (super.getDir()) {
			case "North":
				super.setDir("West");
				break;
			case "South":
				super.setDir("East");
				break;
			case "East":
				super.setDir("North");
				break;
			case "West":
				super.setDir("South");
				break;

			}
		}
		if (dir.equals("Right")) {
			switch (super.getDir()) {
			case "North":
				super.setDir("East");
				break;
			case "South":
				super.setDir("West");
				break;
			case "East":
				super.setDir("South");
				break;
			case "West":
				super.setDir("North");
				break;

			}
		}

	}

	@Override
	public void moveForward() {
		move("Forward");

	}

	@Override
	public void moveBackwards() {
		move("Backwards");

	}

	@Override
	public void lookLeft() {
		move("Left");

	}

	@Override
	public void lookRight() {
		move("Right");
	}

	@Override
	public void disconnect() {
		connected = false;


	}

	@Override
	public void sendMessage(String message) {
		// TODO Is this needed?

	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	@Override
	public TransmitTile pickupItem(Entity e) throws InteractException {
		if(inventory.size() == 6) throw new InteractException(inventoryFull);
		Tile room = finder.getRoom(super.getPos());

		if (room.hasEntity(e)) {
			if (e instanceof Item) {
				if (((Item) e).pickup()) {
					inventory.add((Item) e);
					room.removeObject(e);
					e.setPos(null);
				} else
					throw new InteractException(notPickup);
			} else
				throw new InteractException(notItem);
		} else
			throw new InteractException(notInRoom);
		return new TransmitTile(room, super.getPos());
	}

	@Override
	public TransmitTile dropItem(Entity e, Integer x, Integer y)
			throws InteractException {
		if (inventory.contains(e)) {
			inventory.remove(e);
			Tile r = finder.getRoom(super.getPos());
			r.addObject(e);
			e.setViewX(x);
			e.setViewY(y);
			r.setupEntity(e, x, y, super.getDir());
			return (new TransmitTile(r, super.getPos()));
		}
		throw new InteractException(4);

	}

	public boolean equals(Object o) {
		if (!(o instanceof Player))
			return false;
		Player p = (Player) o;
		if (!super.equals(p))
			return false;
		if (!inventory.equals(p.getInventory()))
			return false;
		if(connected != p.isConnected()) return false;
		return true;
	}

	@Override
	public Entity getEntity() {
		return null;
	}

	@Override
	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("inventory", inventory);
		s.addField("connected", connected);
		s.addReference("finder", finder);
		return s;
	}

	@SuppressWarnings("unchecked")
	public Player(SaveData s) {
		super(s.getSuperData());
		this.inventory = (ArrayList<Item>) s.getField("inventory");
		this.connected =  (boolean) s.getField("connected");
	}

	@Override
	public void linkReference(String name, Object refersTo) {
		if (name.equals("finder")) {
			finder = (GameLogic) refersTo;
		}
	}
	@Override
	public String toString() {
		return "Player getDir()=" + getDir()
				+ ", getPos()=" + getPos()
				+ ", getName()=" + getName() + ", getDescription()="
				+ getDescription() + ", getViewX()=" + getViewX()
				+ ", getViewY()=" + getViewY() + "]";
	}
	//Testing
	public void addItem(Item i){
		inventory.add(i);
	}

	public boolean isConnected() {
		return connected ;
	}

	public void connect() {
		connected = true;

	}
	public String getPlayerDirection()
	{
		return(super.getDir());
	}

}
