package gameLogic;

import gameLogic.interfaces.Key;
import gameLogic.interfaces.Openable;

import java.awt.Point;

import storage.Linkable;
import storage.SaveData;

/**
 * Item subclass that is a key
 *
 * @author Alex Dent
 *
 */
public class IKey extends Item implements Key, Linkable {
	private static final long serialVersionUID = -860932880392963802L;
	private Openable opens;

	public IKey(String name, String des, Point pos, String fn, int y,
			boolean pickup) {
		super(name, des, pos, fn, y, pickup);
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
		s.addReference("opens", opens);
		return s;
	}

	public IKey(SaveData s) {
		super(s.getSuperData());
		opens = (Openable) s.getField("opens");
	}

	public boolean equals(IKey i) {
		if (!super.equals(i))
			return false;
		if (!opens.equals(i.getOpens()))
			return false;
		return true;
	}

	@Override
	public void linkReference(String name, Object refersTo) {
		if (name == "opens") {
			opens = (Openable)refersTo;
		}
	}


}
