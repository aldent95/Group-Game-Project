package render;

import gameLogic.GameLogic;
import gameLogic.Wall;
import gameLogic.interfaces.Drawable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class RenderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private boolean messageExpired= true;
	private LinkedList<String> messageQueue = new LinkedList<String>();
	private String currentMessage = "";
	private String playerDirection = "";
	ArrayList<Drawable> toRender = new ArrayList<Drawable>();

	/**
	 * Main class for Rendering.
	 * This panel creates and displays Drawables as RenderLabels
	 *
	 * @param width
	 *            width of the RenderPanel
	 * @param height
	 * 			  height of the RenderPanel
	 *
	 * @author Glen Peek 300283786
	 */
	public RenderPanel(int width, int height){
		this.setLayout(null);
		this.setSize(width,height);
		this.setVisible(true);
	}

	/**
	 * Updates the Render Panel
	 *
	 * @param toRender list of Drawables to be rendered
	 *
	 * @author Glen Peek
	 */
	public void update(ArrayList<Drawable> toRender,String direction){
		this.playerDirection = direction;
		this.toRender = toRender;
		Boolean hitWall = false;
		this.removeAll();

		for(Drawable e: toRender){
			if(hitWall)
				this.add(new RenderLabel(e,true));
			else
				this.add(new RenderLabel(e,false));

			if(e instanceof Wall)
				hitWall = true;
		}

		if(hitWall)
			this.add(new RenderLabel(new Wall("The Wall", "Wall Description","img/walls/distance.png"),false));

        this.validate();
        this.repaint();
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(messageQueue.size() > 0 && messageExpired)
		{
			currentMessage = messageQueue.pop();
			(new Thread(){
				@Override
				public void run() {
					RenderPanel.this.messageExpired = false;
					try {sleep(1000);} catch (InterruptedException e) {}
					RenderPanel.this.messageExpired = true;
					RenderPanel.this.currentMessage = "";
					RenderPanel.this.repaint();
				}
			}).start();
		}
		g.setColor(Color.white);

		ImageIcon i = new ImageIcon(getClass().getResource("/render/img/compass/" + playerDirection.toLowerCase() + ".png"));
		g.drawImage(i.getImage(),0, 300, null);
		drawString(currentMessage, g);

	}
	/**
	 * Draws a string on the screen.  Used to display object names and descriptions.
	 *
	 * @param str 	The string to draw on the screen
	 * @param g 	The graphics on which to draw the string
	 */
	private void drawString(String str,Graphics g)
	{
		float size = 50;
		FontMetrics m = g.getFontMetrics();
		do{
			g.setFont(g.getFont().deriveFont(size--));
			m = g.getFontMetrics();
		}while(m.stringWidth(str) > getWidth());

		g.drawString(str, getWidth() / 2 - (m.stringWidth(str) / 2), getHeight() - 10);
	}

	/**
	 * Queues messages to display while ignoring duplicates
	 *
	 * @param msg  message to be added to the queue of messages
	 */
	public void queueMessage(String msg){
		if(messageQueue.contains(msg))
			return;
		messageQueue.addLast(msg);
		repaint();
	}

	public JPanel getRenderPanel() {
		return this;
	}

}
