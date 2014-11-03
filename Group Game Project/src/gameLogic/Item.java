package gameLogic;

import java.awt.Point;

import storage.SaveData;
import storage.Saveable;

/**
 * Item class, used to define a basic item and all its methods
 *
 * @author Alex Dent
 *
 */
public class Item extends Entity implements Saveable {
	private boolean canPickup;
	/**
	 *
	 */
	private static final long serialVersionUID = 7520578866085734078L;

	/**
	 * Item constructor
	 *
	 * @param name
	 *            - String, name of the item
	 * @param des
	 *            - String, description of the item
	 * @param pos
	 *            - Point, position of the item within a room, null if in an
	 *            inventory
	 * @param fn
	 *            - String, the filename of the items image
	 * @param y
	 *            - int The height at which the renderer draws the image
	 * @param p
	 *            - boolean ture/false state of the item being able to be picked
	 *            up
	 */
	public Item(String name, String des, Point pos, String fn, int y, boolean p) {
		super(name, des, pos, fn, y);
		this.canPickup = p;
	}

	/**
	 * Used to determin if an item can be picked up
	 *
	 * @return boolean
	 */
	public Boolean pickup() {
		return canPickup;
	}

	@Override
	public Entity getEntity() {
		if (canPickup) {
			return this;
		}
		return null;
	}

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("canPickup", canPickup);
		return s;
	}

	public Item(SaveData s) {
		super(s.getSuperData());
		canPickup = (boolean) s.getField("canPickup");
	}

	public boolean equals(Item i) {
		if (!super.equals(i))
			return false;
		if (canPickup != i.canPickup)
			return false;
		return true;
	}

}
