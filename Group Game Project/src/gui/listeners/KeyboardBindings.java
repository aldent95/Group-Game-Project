package gui.listeners;

import gameLogic.Entity;
import gameLogic.GameLogic;
import gui.Frame;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import networking.interfaces.GameInputAcceptor;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import render.RenderPanel;



/***
 *
 * This Class moniters the Keyboard input for the game
 *
 * @author Andrew Booker
 *
 */
public class  KeyboardBindings {
	private GameInputAcceptor inputChannel;
	private RenderPanel renderPanel;
	private Entity item;
	private InputMap im;
	private ActionMap am;
	private Frame frame;

	public KeyboardBindings(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame) {
	this.renderPanel = renderPanel;
	this.inputChannel =inputChannel;
	this.frame = frame;
	}


	/**
	 * This is a blank constructer that helps initilizing a blank class
	 *
	 */
	public KeyboardBindings(){}

/**
 * This adds a key binding to JPanel that gets past when you create this class
 * you add the method with the same name as cmd here so it calls the method when the key is pressed
 *
 * @param key - the keyevent you want to bind the action to
 * @param cmd - the name of the method in this class
 * @throws NoSuchMethodException
 * @throws SecurityException
 * @throws ClassNotFoundException
 */
	public void addKeyBinding( KeyStroke key ,final String cmd) throws NoSuchMethodException, SecurityException, ClassNotFoundException{
		final Method method = this.getClass().getMethod(cmd,RenderPanel.class, GameInputAcceptor.class,Frame.class);
		method.setAccessible(true);
		im.put(key, cmd);
		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						// this library is to help create a blank instance of the class
						Objenesis test = new ObjenesisStd();
						ObjectInstantiator<KeyboardBindings> thingyInstantiator = test.getInstantiatorOf(KeyboardBindings.class);
						KeyboardBindings bindings = (KeyboardBindings)thingyInstantiator.newInstance();
						method.invoke(bindings,renderPanel,inputChannel,frame);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException e1) {
						e1.printStackTrace();
					}


			}
		};
		am.put(cmd, action);
	}

	public void setrenderwindowandInputChannel( RenderPanel renderPanel,GameInputAcceptor inputChannel,Frame frame){
		this.renderPanel = renderPanel;
		this.inputChannel =inputChannel;
	}

	public void setItem(Entity item) {
		this.item = item;
	}
	public Entity getItem() {
		return item;
	}


	/***
	 *
	 * this is where you add the keys you want it to listen to.
	 *
	 *
	 * @param j - this is the JPanel you want to set up the key bindings on
	 * @return A JpPanel a has the key binidings set to it
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	public JPanel setUpBindings(JPanel panel) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		JPanel j = panel;
		im = j.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		am = j.getActionMap();

		// add new keys here
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "rightArrow");
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "leftArrow");
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "upArrow");
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "downArrow");
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_E,0),"eInteract");
		addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_Q,0), "qInteract");

		return j;

	}
	/*
	 *
	 * From here are the methods that you create  when you want to add a keybinding.
	 *
	 *
	 *
	 */


	public void leftArrow(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){
		inputChannel.lookLeft();
		}

	public void rightArrow(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){
		inputChannel.lookRight();
		}

	public void upArrow(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){
		inputChannel.moveForward();
		}

	public void downArrow(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){
		inputChannel.moveBackwards();
		}

	public void qInteract(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, renderPanel);
		// checks to see if mouse listener within the bouns of the renderpanel
		if ((p.x > 0 && p.x < 800) && (p.y > 0 && p.y < 800)){

		}
		if(frame.getInventoryCanvas().getSelectedItem().getItem()==null)
		{
		frame.addToNotificationText("You can not drop this item");
		}else{
			if ((p.x > 0 && p.x < 800) && (p.y > 0 && p.y < 450)){
			Entity temp = (Entity)frame.getInventoryCanvas().getSelectedItem().getItem();
			inputChannel.dropItem(temp,p.x,p.y);
			}
		}




		}




	public void eInteract(RenderPanel renderPanel, GameInputAcceptor inputChannel , Frame frame){

		if(frame.getInventoryCanvas().getInventoryBoxes().get(6).getItem()==null)
		{frame.addToNotificationText("You can not pick up this item");
		}else{
			inputChannel.pickupItem((Entity)frame.getInventoryCanvas().getInventoryBoxes().get(6).getItem());
			frame.getInventoryCanvas().getInventoryBoxes().get(6).setItem(null);
		}

	frame.getInventoryCanvas().repaint();
	}



	}



