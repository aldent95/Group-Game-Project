package gameLogic;

import java.awt.Point;

import storage.SaveData;
import storage.Saveable;

/**
 * Abstract character class, used for players and was also intended to be used
 * by NPCs
 *
 * @author Alex Dent
 *
 */
public abstract class Character extends Entity implements Saveable {
	private static final long serialVersionUID = -2469258580780958321L;

	private String dir = "North";

	/**
	 * Creates a new character
	 *
	 * @param name
	 *            - The name for the new Character
	 * @param des
	 *            - The description for the new player
	 * @param pos
	 *            - The starting room coords for the new player
	 * @param fileName
	 *            - The filename for the image of the player
	 * @param height
	 *            - The height at which the render engine will draw the image
	 */
	public Character(String name, String des, Point pos, String fileName,
			int height) {
		super(name, des, pos, fileName, height);
	}

	protected abstract void move(String dir);

	protected String getDir() {
		return dir;
	}

	protected void setDir(String dir) {
		this.dir = dir;
	}

	// TODO Andrew, can I have some javadoc for this?
	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.setSuperData(super.getSaveData());
		s.addField("dir", dir);
		return s;
	}

	// TODO Andrew, can I have some javadoc for this?
	Character(SaveData s) {
		super(s.getSuperData());
		this.dir = (String) s.getField("dir");
	}

	// TODO Andrew, can I have some javadoc for this?
	public boolean equals(Character c) {
		if (!super.equals(c))
			return false;
		if (!dir.equals(c.dir))
			return false;
		return true;
	}

}
