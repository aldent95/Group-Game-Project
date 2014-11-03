package gameLogic;

import gameLogic.interfaces.Container;
import gameLogic.interfaces.Key;
import gameLogic.interfaces.Openable;
import gameLogic.interfaces.Treasure;

import java.awt.Point;
import java.util.ArrayList;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is both a Key, a Treasure and a Container
 *
 * @author Alex Dent
 *
 */
public class IAll extends Item implements Key, Treasure, Container, Saveable,
		Openable {

	private static final long serialVersionUID = 9139112207161180851L;
	private ArrayList<Item> contents;
	private int value;
	private Openable opens;

	public IAll(String name, String des, Point pos, String fn, int y,
			boolean pickup) {
		super(name, des, pos, fn, y, pickup);
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
		s.addField("contents", contents);
		s.addField("value", value);
		s.addReference("opens", opens);
		return s;
	}

	@SuppressWarnings("unchecked")
	public IAll(SaveData s) {
		super(s.getSuperData());
		contents = (ArrayList<Item>) s.getField("contents");
		opens = (Openable) s.getField("opens");
		value = (int) s.getField("value");
	}

	public boolean equals(IAll i) {
		if (!super.equals(i))
			return false;
		if (!contents.equals(i.getContents()))
			return false;
		if (value != i.getValue())
			return false;
		if (!opens.equals(i.opens))
			return false;
		return true;
	}

}
