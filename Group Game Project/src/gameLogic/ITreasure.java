package gameLogic;

import gameLogic.interfaces.Treasure;

import java.awt.Point;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is a Treasure
 *
 * @author Alex Dent
 *
 */
public class ITreasure extends Item implements Treasure, Saveable {

	private static final long serialVersionUID = 3739926169190089375L;
	private int value;

	public ITreasure(String name, String des, Point pos, String fn, int y,
			boolean pickup) {
		super(name, des, pos, fn, y, pickup);
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int i) {
		value = i;
	}

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("value", value);
		return s;
	}

	public ITreasure(SaveData s) {
		super(s.getSuperData());
		value = (int) s.getField("value");
	}

	public boolean equals(ITreasure i) {
		if (!super.equals(i))
			return false;
		if (value != i.getValue())
			return false;
		return true;
	}

}
