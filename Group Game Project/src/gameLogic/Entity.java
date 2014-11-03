package gameLogic;

import gameLogic.interfaces.Drawable;

import java.awt.Point;
import java.io.Serializable;

import storage.SaveData;
import storage.Saveable;
/**
 * Abstract class that defines the behavior for all entitys in the game
 * @author Alex Dent
 *
 */
public abstract class Entity implements Saveable, Drawable, Serializable {
	@Override
	public String toString() {
		return "Entity [pos=" + pos + ", name=" + name + ", image=" + image
				+ "]";
	}
	private static final long serialVersionUID = 5631464914299367356L;

	private Point pos;
	private String name;
	private String description;
	private String image;
	private int viewX;
	private int viewY;

	/**
	 * Used to setup fields in entity when creating a subclass instance
	 *
	 * @param name
	 *            - The name of the entity
	 * @param des
	 *            - The description of the entity
	 * @param pos
	 *            - The position of the entity in a room
	 * @param fileName
	 *            - The filename string of the image for the entity
	 * @param height
	 *            - The height the render engine draws the image
	 */
	public Entity(String name, String des, Point pos, String fileName, int height) {
		this.name = name;
		this.description = des;
		this.pos = pos;
		this.image = fileName;
		this.viewY = height;
	}

	public String getImage() {
		return image;
	}
	/**
	 * Used to get the position of the entity in a room
	 * @return Point - the pos of the object
	 */
	public Point getPos() {
		return pos;
	}

	protected void setPos(Point pos) {
		this.pos = pos;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getViewX() {
		return viewX;
	}

	protected void setViewX(int viewX) {
		this.viewX = viewX;
	}

	public int getViewY() {
		return viewY;
	}

	protected void setViewY(int viewY) {
		this.viewY = viewY;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Entity)) return false;
		Entity e = (Entity) obj;
		if(!(pos == null && e.getPos() == null))
		{
			if(!pos.equals(e.getPos())) return false;
		}
		if(!name.equals(e.getName())) return false;
		if(!description.equals(e.getDescription())) return false;
		return true;
	}
	@Override
	public boolean equals(Drawable obj) {
		if(!(obj instanceof Entity)) return false;
		Entity e = (Entity) obj;
		if(!(pos == null && e.getPos() == null))
		{
			if(!pos.equals(e.getPos())) return false;
		}
		if(!name.equals(e.getName())) return false;
		if(!description.equals(e.getDescription())) return false;
		return true;
	}
	//TODO Andrew, can I have some javadoc for this?
	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.addField("position", pos);
		s.addField("name", name);
		s.addField("description", description);
		s.addField("image", image);
		s.addField("viewX", viewX);
		s.addField("viewY", viewY);
		return s;
	}
	//TODO Andrew, can I have some javadoc for this?
	Entity(SaveData s){
		this.pos = (Point)s.getField("position");
		this.name = (String)s.getField("name");
		this.description = (String)s.getField("description");
		this.image = (String)s.getField("image");
		this.viewX = (int)s.getField("viewX");
		this.viewY = (int)s.getField("viewY");
	}

}
