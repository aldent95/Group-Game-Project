package networking;

import gameLogic.Entity;
import gameLogic.GameLogic;
import gameLogic.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import networking.SocketThread.SocketHandler;
import networking.interfaces.GameInputAcceptor;

/**
 * The gameClient package is the binding between the game logic and the networked game logic held on the server.
 * Every game input is received here in ClientTransmitter and is sent to the server.
 * Every server command is received in ClientReceiver and is sent back to GameLogic and then redrawn.
 * @author Michael Pearson
 */
public class GameClient implements SocketHandler {
	/**
	 * Defines the interface for when the client is ready to do things (like redraw and opening the frame) with the gui.
	 * @author Michael Pearson
	 */
	public interface GameReadyCallback
	{
		public void gameReady(GameLogic game,GameInputAcceptor inputChannel);
		public void redraw();
		public void displayMessage(String message);
	}
	private String ipEndpoint;
	private int portEndpoint;
	private Socket socket;
	private GameInputAcceptor inputChannel;
	private GameLogic game;
	private GameReadyCallback gameReadyCallback;
	private ClientReceiver clientReceiver;

	/**
	 * @param ip The IP Address of the server
	 * @param port The port the server is listening on
	 */
	public GameClient(String ip,int port) {
		ipEndpoint = ip;
		portEndpoint = port;
		clientReceiver = new ClientReceiver();
	}
	/**
	 * This method will connect to the server!
	 * Warning this method is blocking so run it in a thread!
	 *
	 * True for successful connection false for no connection or error.
	 * @return
	 */
	public boolean connect()
	{
		try {
			socket = new Socket(ipEndpoint,portEndpoint);
			(new SocketThread(socket, this)).start();
			inputChannel = new ClientTransmitter(socket);
			return(socket.isConnected());
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return(false);
		}
	}
	/**
	 * This function will send the required data to the server in order to get the game logic and player classes back.
	 * @param name Player name
	 * @param description Player description
	 * @param cb a ready callback to use throughout the game lifecycle
	 */
	public void initGame(String name,String description,GameReadyCallback cb)
	{
		gameReadyCallback = cb;
		RPC.sendCall(socket, "initGame",name,description);
	}

	/**
	 * Just a handy wrapper for the RPC function.
	 * @param functionName
	 * @param args
	 */
	public void sendCall(String functionName,Object...args)
	{
		RPC.sendCall(socket, functionName);
	}
	/**
	 * Simple getter for the game state received from the network.
	 * @return
	 */
	public GameLogic getGameState(){return(game);}

	/**
	 * Gets the GameInputAcceptor interface. The "inputChannel" returned will send commands over the network to the server and back to the client. Any command on the returned object will affect the state of the game on client and server.
	 * @return
	 */
	public GameInputAcceptor getInputChannel(){return(inputChannel);}

	@Override
	/**
	 * To be called when data is avaliable.<br />
	 * {@inheritDoc}
	 */
	public void dataReady(byte[] input, OutputStream outputStream) {
		RPC.reCreate(input).execute(clientReceiver);
	}
	@Override
	/**
	 * To be called when the socket is closed or disconnected <br />
	 * {@inheritDoc}
	 */
	public void disconnect() {
		System.out.println("Shutting down client");
		System.exit(0);
	}
	/**
	 * This object is created once to take input from the server and inject it into the local game.
	 * All functions in this object are always called from reflection so stop teling me that there unused elcipse!
	 */
	@SuppressWarnings("unused")
	private class ClientReceiver
	{
		/**
		 * Receives the game logic from the server
		 * @param game
		 */
		public void setGameLogic(GameLogic game)
		{
			GameClient.this.gameReadyCallback.gameReady(game,GameClient.this.inputChannel);
			GameClient.this.game = game;
		}
		/**
		 * injectes the neqw state of the player into the local game
		 * @param player
		 */
		public void updatePlayer(Player player)
		{
			GameClient.this.game.updatePlayer(player);
			GameClient.this.gameReadyCallback.redraw();
		}
		/**
		 * Injectes the new state of a game tile into the local game.
		 * @param tile
		 */
		public void updateTile(TransmitTile tile)
		{
			GameClient.this.game.updateRoom(tile);
		}
		/**
		 * Something the player did triggered an exception. Print it out on the client.
		 * @param e
		 */
		public void exceptionEncountered(Exception e)
		{
			GameClient.this.gameReadyCallback.displayMessage(e.getMessage());
			//System.err.println("Client got an exception! The exception was " + e.getMessage());
		}
	}
	/**
	 * This class transmits player actions to the server. For every call to this class it is just relayed to the server through a RPC call.
	 * @author Michael Pearson
	 */
	private class ClientTransmitter implements GameInputAcceptor{
		private Socket socket;
		public ClientTransmitter(Socket socket) {this.socket = socket;}
		@Override
		public void sendMessage(String message) {RPC.sendCall(socket, "sendMessage", message);}
		@Override
		public TransmitTile pickupItem(Entity e) {RPC.sendCall(socket,"pickupItem",e);return(null);}
		@Override
		public void moveForward() {call("moveForward");}
		@Override
		public void moveBackwards() {call("moveBackwards");}
		@Override
		public void lookRight() {call("lookRight");}
		@Override
		public void lookLeft() {call("lookLeft");}
		@Override
		public void disconnect() {
			call("disconnect");
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Could not close socket on client end");
			}
		}
		public void call(String functionName){RPC.sendCall(socket,functionName);}
		@Override
		public TransmitTile dropItem(Entity e, Integer x, Integer y) {RPC.sendCall(socket,"dropItem",e,x,y);return(null);}
	}
}