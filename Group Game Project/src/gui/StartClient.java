package gui;


import gameLogic.GameLogic;
import gui.listeners.SplashGraphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import networking.GameClient;
import networking.GameClient.GameReadyCallback;
import networking.interfaces.GameInputAcceptor;

/**
 *
 * creates the client for the game
 *
 * @author Andrew booker
 */
public class StartClient {
	private JFrame window = new JFrame();
	private SplashPagePanel panel;
	private SplashGraphics inventoryCavas;
	private Frame gameFrame;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		StartClient s = new StartClient();
	}

/**
 *
 * Creates the splash page and the frame
 *
 *
 */
	public StartClient() {

		window.setSize(600,500);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel label = new JLabel("Player Name:");
		panel = new SplashPagePanel(new ConnectAction() {
			@Override
			public void connect(String ip, int port,String name,String description) {
				new StartClient(ip,port,name,description);
				StartClient.this.window.dispose();
			}

			@Override
			public void startServer(int port) {
				new StartServer(port);
				StartClient.this.window.dispose();
			}
		}, window);

		panel.add(label);
		inventoryCavas = new SplashGraphics();
		JSplitPane split_inv = new JSplitPane(JSplitPane.VERTICAL_SPLIT,inventoryCavas, panel);
		split_inv.setDividerLocation(200);
		split_inv.setEnabled(false);

		window.add(split_inv);
		window.setVisible(true);
	}
/**
 * this will be called from the custom panel if you ever want to connect to a server you just created
 *
 *
 * @param ip - the ipaddres
 * @param port - the port you want to connect to
 * @param name - the name of your player
 * @param description - the decription of your player
 */
	public StartClient(String ip, int port,String name,String description){
		// this will be called from the custom panel if you ever want to connect to a server you just created
		final GameClient client = new GameClient(ip,port); //Gets the main network client.

		(new ConnectToServer(client,name,description)).execute();
	}

	private class ConnectToServer extends SwingWorker<Void, Void>
	{
		GameClient client;
		String name,description;
		public ConnectToServer(GameClient client,String name,String description) {
			this.client = client;
			this.name = name;
			this.description = description;
		}
		@Override
		protected Void doInBackground() throws Exception {
			client.connect();
			client.initGame(name, description,new GameReadyCallback() {
				private GameLogic logic;
				@Override
				public void gameReady(GameLogic game, GameInputAcceptor inputChannel) {
					logic = game;
					StartClient.this.gameFrame = new Frame(game, inputChannel);
				}
				public void redraw()
				{
					StartClient.this.gameFrame.getRenderPanel().update(logic.getView(800),logic.getPlayer().getPlayerDirection());
					StartClient.this.gameFrame.getInventoryCanvas().refreshInven();
				}
				@Override
				public void displayMessage(String message) {
					StartClient.this.gameFrame.addToNotificationText(message);
				}
			});
			return null;
		}
	}
}
