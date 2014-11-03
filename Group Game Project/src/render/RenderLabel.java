package render;

import gameLogic.Entity;
import gameLogic.interfaces.Drawable;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RenderLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	private String description;
	private Drawable drawable;

	/**
	 * A renderable object containing a drawable with a name, description, and image
	 * 
	 * @param drawable to be rendered
	 * @param inNextRoom true if object is one room away
	 * 
	 * @author Glen Peek 300283786
	 */
	public RenderLabel(Drawable d, Boolean inNextRoom) {
		this.drawable = d;
		ImageIcon i = new ImageIcon(this.getClass().getResource(drawable.getImage()));
		if(inNextRoom){
			i = new ImageIcon(i.getImage().getScaledInstance(i.getIconWidth()/2,i.getIconHeight()/2, Image.SCALE_SMOOTH));
		}
		this.setIcon(i);
		this.setLocation(drawable.getViewX(), drawable.getViewY());
		
		if(inNextRoom){
			this.setLocation(drawable.getViewX()/2+200, drawable.getViewY()/2+113);
		}
		
		this.setSize(i.getIconWidth(), i.getIconHeight());

		this.setName(drawable.getName());
		this.setDescription(drawable.getDescription());
	}

	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Entity getEntity(){
		return drawable.getEntity();
	}

}
