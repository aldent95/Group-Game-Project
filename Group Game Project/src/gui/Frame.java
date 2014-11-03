package gui;

import gameLogic.GameLogic;
import gui.listeners.InventoryCanvasMouseListener;
import gui.listeners.KeyboardBindings;
import gui.listeners.MenuBarActionListener;
import gui.listeners.TopCanvasMouseListener;

import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import networking.interfaces.GameInputAcceptor;
import render.RenderPanel;



/**
 * this is the frame that holds the client and the connections to the server
 *
 * @author Andrew Booker
 *
 */
public class Frame extends JFrame {
	//varibles
	private static final long serialVersionUID = 1L;
	private Frame frame;
	private RenderPanel  renderWindow = null;
	private KeyboardBindings bindings;
	private InventoryCanvas inventoryCanvas;
	private GameLogic game;
	private GameInputAcceptor inputChannel;
	private JPanel keybindingsPanel;
	private TopCanvasMouseListener mouseListener;

	/**
	 * constructor that creates the frame
	 *
	 *
	 * @param game -gamelogic
	 * @param inputChannel
	 */
	public Frame(GameLogic game, GameInputAcceptor inputChannel) {
		this.game = game;
		this.inputChannel = inputChannel;

		constructFrame();
	}
	/**
	 *
	 * This creates the frame then calls the appropreiate methods that set up the frame
	 *
	 *
	 *
	 */
	private void constructFrame() {
		frame = this;
		frame.setTitle("Art Heist");
		frame.setSize(800, 657);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setupFrameComponents();
		bindings.setrenderwindowandInputChannel(renderWindow, inputChannel, frame);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	/**
	 * The order for this method is very Important to the correct set up of the frame!
	 *
	 */
	private void setupFrameComponents() {
		menuCreation();
		setUpKeyBindingAndListeners();
		createCanvas();
		createInventory();
	}
	/**
	 * creates the menu Bar and the items
	 *
	 */
	private void menuCreation() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Menu");
		MenuBarActionListener list = new MenuBarActionListener(this);

		JMenuItem exitGame = new JMenuItem("Exit");
		exitGame.addActionListener(list);
		fileMenu.add(exitGame);


		JMenu helpMenu = new JMenu("Help");

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(list);
		helpMenu.add(about);

		JMenuItem help = new JMenuItem("Help");
		help.addActionListener(list);
		helpMenu.add(help);


		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);

	}
	/**
	 * This method is responsible for setting up the keybindings and listeners for the program
	 *
	 */

	private void setUpKeyBindingAndListeners() {
		keybindingsPanel = new JPanel();
		 bindings = new KeyboardBindings(renderWindow,inputChannel,this);
		try {
			keybindingsPanel = bindings.setUpBindings(keybindingsPanel);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		frame.add(keybindingsPanel);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				boolean confirmBox = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to quit?", "Confirm exit.",
						JOptionPane.YES_NO_OPTION, 0, new ImageIcon("")) == JOptionPane.NO_OPTION;
				if (!confirmBox) {
					inputChannel.disconnect();
					frame.dispose();
				}
			}});

	}
	/**
	 *
	 * creates the RenderWindow Canvas
	 */
	private void createCanvas() {
		renderWindow = new RenderPanel(800, 450);
		JPanel renderPanel = renderWindow.getRenderPanel();
		renderWindow.update(game.getView(800),game.getPlayer().getPlayerDirection());
		MouseListener mouseListener = new TopCanvasMouseListener(renderPanel, game,this);
		renderPanel.addMouseListener(mouseListener);
		frame.addMouseListener(mouseListener);
		frame.add(renderPanel);
	}


	/**
	 *
	 * Creates the inventory canvas and adds it to the frame
	 *
	 */
	private void createInventory() {
		inventoryCanvas = new InventoryCanvas(this);
		InventoryCanvasMouseListener mouse = new InventoryCanvasMouseListener(inventoryCanvas);
		inventoryCanvas.addMouseListener(mouse);
		frame.addMouseListener(mouse);

		JSplitPane split_inv = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				renderWindow,inventoryCanvas);
		split_inv.setDividerLocation(450);
		split_inv.setDividerSize(1);
		split_inv.setEnabled(false);

		keybindingsPanel.requestFocus();
		frame.add(split_inv);

	}
	public GameInputAcceptor getInputChannel() {
		return inputChannel;
	}
	public InventoryCanvas getInventoryCanvas() {
		return inventoryCanvas;
	}
	public GameLogic getGame() {
		return game;
	}
	public TopCanvasMouseListener getMouseListener() {
		return mouseListener;
	}

	public void setMouseListener(TopCanvasMouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public RenderPanel getRenderPanel() {
		return (renderWindow);
	}
	/**
	 *
	 * Call this to add text on the screen
	 *
	 *
	 * @param text - the text to be shown on the screen
	 */
	public void addToNotificationText(String text) {
		renderWindow.queueMessage(text);
	}
}

