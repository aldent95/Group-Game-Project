package gameLogic;

import gameLogic.interfaces.Container;
import gameLogic.interfaces.Openable;

import java.awt.Point;
import java.util.ArrayList;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is a container
 *
 * @author Alex Dent
 *
 */
public class IContainer extends Item implements Container, Saveable, Openable {
	/**
	 *
	 */
	private static final long serialVersionUID = 502552384239974302L;
	private ArrayList<Item> contents;

	public IContainer(String name, String des, Point pos, String fn, int y,
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

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("contents", contents);
		return s;
	}

	@SuppressWarnings("unchecked")
	public IContainer(SaveData s) {
		super(s.getSuperData());
		contents = (ArrayList<Item>) s.getField("contents");
	}

	public boolean equals(IContainer i) {
		if (!super.equals(i))
			return false;
		if (!contents.equals(i.getContents()))
			return false;
		return true;
	}

}
