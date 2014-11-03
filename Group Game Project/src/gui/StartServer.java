package gui;


import gameLogic.GameLogic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import storage.Loader;
import storage.Saver;
import networking.GameServer;
import networking.GameServer.NetworkStatusProxy;
import networking.ServerPlayer;


/**
 * this class is creates the server for players to connect to.
 *
 *
 *
 * @author Andrew Booker and michael
 *
 */
public class StartServer {
	//TODO set up server and start client classes so they can be run from the Cmd and batch files
	private JTextArea serverText;
	private int port = 3333;
	private GameServer server;
	private JFrame frame;

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
/**
 * this is a constructor to create a server with a custom port and ip
 * @param ip
 * @param port
 */
	public StartServer(int port){
		this.port = port;
		boolean serverFrame = createFrame();
		if(serverFrame){
			setUpServer();
		}

	}


	public StartServer() {
		boolean serverFrame = createFrame();
		if(serverFrame){
			setUpServer();
		}
	}


	void setUpServer() {
		GameLogic gameLogic = Loader.loadGameLogic(Saver.DEFAULT_SAVE_FILENAME);
		server = new GameServer(new NetworkStatusProxy() {
			@Override
			public void sendMessage(String message) {
				StartServer.this.addToServerText(message,false);

			}

			@Override
			public void clientDisconnect(ServerPlayer client) {
				sendMessage("Client Disconnect");

			}
		},this.port,gameLogic);
	}


	private boolean createFrame() {
		frame = new JFrame("Server");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		serverText = serverText(frame);
		  frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					boolean confirmBox = JOptionPane.showConfirmDialog(frame,
							"Are you sure you want to shut down the server?", "Confirm Shutdown.",
							JOptionPane.YES_NO_OPTION, 0, new ImageIcon("")) == JOptionPane.NO_OPTION;

					if (!confirmBox) {
						shutdownServer();
					}

			}});
		frame.setVisible(true);
		frame.setResizable(false);
		return true;
	}
/**
 *
 * Shuts down the server
 *
 *
 */
	private void shutdownServer()
	{
		server.shutdown();
		boolean serverSaves = true; // will later on save the server and return true if it was sussecful
		if (serverSaves)
		{
			addToServerText("Server has been saved to location that you chose");
		}else
		{
			addToServerText("Server did not Save correctly");
		}

		frame.dispose();
		System.out.println("Shutting down server");
		System.exit(0);
	}
	private static JTextArea serverText(JFrame frame) {
		JTextArea text = new JTextArea();
		JScrollPane scroll = new JScrollPane(text);
		frame.add(scroll);
		text.setEditable(false);
		return text;
	}
	public void addToServerText(String text) {
		addToServerText(text,true);
	}
	public void addToServerText(String text,boolean newLine) {
		if(newLine)
		{
			serverText.append(text + "\n");
			serverText.setCaretPosition(serverText.getText().length() - 1);
		}
		else
		{
			serverText.append(text);
			serverText.setCaretPosition(serverText.getText().length() - 1);
		}
	}


}
