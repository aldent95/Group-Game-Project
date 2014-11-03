package gameLogic;

import gameLogic.interfaces.Drawable;
import gameLogic.interfaces.Openable;

import java.io.Serializable;

import storage.Linkable;
import storage.SaveData;
import storage.Saveable;
/**
 * Wall class, used to define a wall and its methods
 * @author Alex Dent
 *
 */
public class Wall implements Drawable,Serializable, Saveable, Linkable, Openable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1099493473181714511L;
	private boolean locked = false;
	private Tile leadsTo = null;
	private boolean isSolid = true;
	private String name;
	private String description;
	private String image;
	private int viewX;
	private int viewY;
	/**
	 * Wall Constructor
	 * @param name - String, name of the wall
	 * @param des - String, description of the wall
	 * @param fileName - String, the filename for the image of the wall
	 */
	public Wall(String name, String des, String fileName) {
		this.setName(name);
		this.setDescription(des);
		this.setImage(fileName);
		this.setViewX(0);
		this.setViewY(0);
	}

	protected boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	private Tile getLeadsTo() {
		return leadsTo;
	}

	public void setLeadsTo(Tile leadsTo) {
		this.leadsTo = leadsTo;
		this.isSolid = false;
	}

	protected boolean isSolid() {
		return isSolid;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}
	private void setImage(String image) {
		this.image = image;
	}

	public int getViewX() {
		return viewX;
	}

	private void setViewX(int viewX) {
		this.viewX = viewX;
	}

	public int getViewY() {
		return viewY;
	}

	private void setViewY(int viewY) {
		this.viewY = viewY;
	}

	@Override
	public Entity getEntity() {
		return null;
	}

	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.addField("name", name);
		s.addField("description", description);
		s.addField("image", image);
		s.addField("viewX", viewX);
		s.addField("viewY", viewY);
		s.addField("locked", isLocked());
		s.addReference("leadsTo", getLeadsTo());
		s.addField("isSolid", isSolid());
		return s;
	}

	public Wall(SaveData s) {
		this.name = (String) s.getField("name");
		this.description = (String) s.getField("description");
		this.image = (String) s.getField("image");
		this.viewX = (int) s.getField("viewX");
		this.viewY = (int) s.getField("viewY");
		locked = (Boolean) s.getField("locked");
		isSolid = (Boolean) s.getField("isSolid");
	}

	@Override
	public void linkReference(String name, Object refersTo) {
		if (name.equals("leadsTo")) {
			leadsTo = (Tile)refersTo;
		}
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Wall){
		Wall w = (Wall) o;
		if(locked != w.locked) return false;
		if(isSolid != w.isSolid) return false;
		if(!name.equals(w.name)) return false;
		if(!description.equals(w.description)) return false;
		if(!image.equals(w.image)) return false;
		return true;
		}
		return false;
	}
	public boolean equals(Drawable w){
		if(!name.equals(w.getName())) return false;
		if(!description.equals(w.getDescription())) return false;
		if(!image.equals(w.getImage())) return false;
		return true;
	}


}
