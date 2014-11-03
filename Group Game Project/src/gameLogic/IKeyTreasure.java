package gameLogic;

import gameLogic.interfaces.Key;
import gameLogic.interfaces.Openable;
import gameLogic.interfaces.Treasure;

import java.awt.Point;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is both a Key and a Treasure
 *
 * @author Alex Dent
 *
 */
public class IKeyTreasure extends Item implements Key, Treasure, Saveable {
	private static final long serialVersionUID = -7412389153482991621L;
	private Openable opens;
	private int value;

	public IKeyTreasure(String name, String des, Point pos, String fn, int y,
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

	@Override
	public Openable getOpens() {
		return opens;
	}

	@Override
	public void setOpens(Openable e) {
		opens = e;
	}

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("value", value);
		s.addReference("opens", opens);
		return s;
	}

	public IKeyTreasure(SaveData s) {
		super(s.getSuperData());
		opens = (Openable) s.getField("opens");
		value = (int) s.getField("value");
	}

	public boolean equals(IKeyTreasure i) {
		if (!super.equals(i))
			return false;
		if (value != i.getValue())
			return false;
		if (!opens.equals(i.opens))
			return false;
		return true;
	}

}
