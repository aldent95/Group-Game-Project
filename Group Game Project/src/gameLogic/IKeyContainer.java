package gameLogic;

import gameLogic.interfaces.Container;
import gameLogic.interfaces.Key;
import gameLogic.interfaces.Openable;

import java.awt.Point;
import java.util.ArrayList;

import storage.SaveData;
import storage.Saveable;

/**
 * Item subclass that is both a Key and a Container
 *
 * @author Alex Dent
 *
 */
public class IKeyContainer extends Item implements Key, Container, Saveable,
		Openable {
	private static final long serialVersionUID = 8302991200700354970L;
	private ArrayList<Item> contents;
	private Openable opens;

	public IKeyContainer(String name, String des, Point pos, String fn, int y,
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
		s.addReference("opens", opens);
		return s;
	}

	@SuppressWarnings("unchecked")
	public IKeyContainer(SaveData s) {
		super(s.getSuperData());
		contents = (ArrayList<Item>) s.getField("contents");
		opens = (Openable) s.getField("opens");
	}

	public boolean equals(IKeyContainer i) {
		if (!super.equals(i))
			return false;
		if (!contents.equals(i.getContents()))
			return false;
		if (!opens.equals(i.opens))
			return false;
		return true;
	}

}
