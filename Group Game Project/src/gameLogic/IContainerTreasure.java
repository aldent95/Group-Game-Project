package gameLogic;

import gameLogic.interfaces.Container;
import gameLogic.interfaces.Openable;
import gameLogic.interfaces.Treasure;

import java.awt.Point;
import java.util.ArrayList;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is both a Container and a Treasure
 *
 * @author Alex Dent
 *
 */
public class IContainerTreasure extends Item implements Container, Treasure,
		Saveable, Openable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2132289846199155712L;
	private ArrayList<Item> contents;
	private int value;

	public IContainerTreasure(String name, String des, Point pos, String fn,
			int y, boolean pickup) {
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
	public ArrayList<Item> getContents() {
		return contents;
	}

	@Override
	public void setContents(ArrayList<Item> items) {
		contents = items;
	}

	@Override
	public void addContents(Item i) {
		contents.add(i);
	}

	@Override
	public boolean removeContents(Item i) {
		return contents.remove(i);
	}

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("contents", contents);
		s.addField("value", value);
		return s;
	}

	@SuppressWarnings("unchecked")
	public IContainerTreasure(SaveData s) {
		super(s.getSuperData());
		contents = (ArrayList<Item>) s.getField("contents");
		value = (int) s.getField("value");
	}

	public boolean equals(IContainerTreasure i) {
		if (!super.equals(i))
			return false;
		if (!contents.equals(i.getContents()))
			return false;
		if (value != i.getValue())
			return false;
		return true;
	}
}
