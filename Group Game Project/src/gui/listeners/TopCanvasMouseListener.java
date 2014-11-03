package gui.listeners;

import gameLogic.Entity;
import gameLogic.GameLogic;
import gui.Frame;
import gui.InventoryCanvas.InventoryBox;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import render.CreateGameWorld;
import render.RenderLabel;
import render.RenderPanel;
/**
 *
 * this class is resposible for handling the mouse event for the top mouse listener
 *
 * @author bookerandr
 *
 */
public class TopCanvasMouseListener implements MouseListener, MouseMotionListener {

	private RenderPanel canvas;
	private GameLogic gameLogic;
	private Frame frame;

	public TopCanvasMouseListener(JPanel j, GameLogic game, Frame frame) {
		this.canvas = (RenderPanel) j;
		this.gameLogic = game;
		this.frame = frame;
		createCursor(frame, "hand.png","Hand");
	}

	/**
	 * Creates a custom mouse cursor
	 *
	 * @param frame the frame where the cursor will appear
	 * @param filename location of the cursor image
	 * @param name	name of the cursors
	 */
	private void createCursor(Frame frame, String filename, String name) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		ImageIcon i = new ImageIcon(getClass().getResource("/render/img/cursor/"+filename));

		Image image = i.getImage();
		Point hotSpot = new Point(16,17);
		Cursor cursor = toolkit.createCustomCursor(image, hotSpot, name);
		frame.setCursor(cursor);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		checkForItem(e);


		frame.getInventoryCanvas().repaint();
	}
/**
 *
 * checks for the label at the current mouse location
 *
 * @param e
 */
	private void checkForItem(MouseEvent e) {
		RenderLabel cl_label = null;
		cl_label = checkForLabel(e, cl_label);

		Entity itemToBePicked = cl_label.getEntity();

		if (itemToBePicked!=null){
			InventoryBox temp = frame.getInventoryCanvas().getInventoryBoxes().get(6);
			temp.setItem(itemToBePicked);
			frame.getInventoryCanvas().getInventoryBoxes().set(6, temp);
		}
	}
	private RenderLabel checkForLabel(MouseEvent e, RenderLabel cl_label) {
		if(e.getComponent().getComponentAt(e.getX(), e.getY()) instanceof RenderLabel){
			cl_label =  (RenderLabel) e.getComponent().getComponentAt(e.getX(), e.getY());
			frame.addToNotificationText(cl_label.getName() + ":\t" + cl_label.getDescription());
		}
		return cl_label;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		createCursor(frame, "closed.png", "Closed");
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		createCursor(frame, "hand.png", "Open");
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		createCursor(frame, "hand.png", "Open");
	}
	@Override
	public void mouseExited(MouseEvent e) {


	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		createCursor(frame,"hand.png", "Open");


	}

}
