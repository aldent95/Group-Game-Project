package networking;

import gameLogic.Entity;
import gameLogic.GameLogic;
import gameLogic.Player;
import gameLogic.InteractException;

import java.awt.Point;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import networking.GameServer.NetworkStatusProxy;
import networking.interfaces.GameInputAcceptor;
import networking.SocketThread.SocketHandler;
/**
 * This class contains the endpoint for the networked commands. ServerReceiver receives commands from the client and takes the required actions.
 * @author Michael Pearson
 */
public class ServerPlayer implements SocketHandler{
	private Player player;
	private GameLogic game;
	private Socket socket;
	private NetworkStatusProxy statusProxy;
	private BroadcastAdaptor broadcast;
	private ServerReceiver receiver;


	interface BroadcastAdaptor {
		public void broadcastPlayer(Player player);
		public void broadcastTile(TransmitTile tile);
	}


	public ServerPlayer(Socket socket,GameLogic game,NetworkStatusProxy statusProxy,BroadcastAdaptor broadcast) {
		this.socket = socket;
		this.statusProxy = statusProxy;
		this.game = game;
		this.broadcast = broadcast;
		this.receiver = new ServerReceiver();
	}
	public void printf(String message,Object... args){statusProxy.sendMessage(String.format(message, args));}
	public void println(String message){statusProxy.sendMessage(String.format("%s\n",message));}




	public void sendPlayerToClient(Player player)
	{
		RPC.sendCall(socket, "updatePlayer", player);
	}
	public void sendTileToClient(TransmitTile tile)
	{
		RPC.sendCall(socket, "updateTile", tile);
	}
	public void sendException(Exception e)
	{
		RPC.sendCall(socket, "exceptionEncountered", e);
	}


	@Override
	public void dataReady(byte[] input, OutputStream outputStream) {
		RPC.reCreate(input).execute(receiver);
	}


	/**
	 * Handles an unexpected socket close
	 */
	@Override
	public void disconnect() {
		player.disconnect();
		statusProxy.clientDisconnect(this);

		broadcast.broadcastPlayer(player);
		try {
			printf("Closing connection to %s:%d\n",socket.getInetAddress().getHostAddress(),socket.getPort());
			socket.close();
		} catch (IOException e) {
			printf("Error Closing socket\n");
			e.printStackTrace();
		}
	}

	/**
	 * This class receives the function calls from ClientTransmitter. It's aim is to apply the command to the player and then broadcast the required updates.
	 * @author Michael Pearson
	 */
	private class ServerReceiver implements GameInputAcceptor
	{
		@Override
		public void moveForward() {
			ServerPlayer.this.player.moveForward();
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}
		@Override
		public void moveBackwards() {
			ServerPlayer.this.player.moveBackwards();
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}
		@Override
		public TransmitTile pickupItem(Entity e) {
			try
			{
				TransmitTile tile = player.pickupItem(e);
				ServerPlayer.this.broadcast.broadcastTile(tile);
				ServerPlayer.this.broadcast.broadcastPlayer(player);
			}
			catch(InteractException x)
			{
				ServerPlayer.this.sendException(x);
			}
			return(null);
		}
		@Override
		public void lookLeft() {
			ServerPlayer.this.player.lookLeft();
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}
		@Override
		public void lookRight() {
			ServerPlayer.this.player.lookRight();
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}

		@Override
		public void sendMessage(String message) {
			ServerPlayer.this.printf("%s\n",message);
		}
		/**
		 * Handles an expected dissconnect request from the client. The client has already closed the socket.
		 */
		@Override
		public void disconnect() {
			ServerPlayer.this.disconnect();
			ServerPlayer.this.player.disconnect();
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}
		@Override
		public TransmitTile dropItem(Entity entity, Integer x, Integer y) {
			try
			{
				TransmitTile tile = ServerPlayer.this.player.dropItem(entity, x, y);
				ServerPlayer.this.broadcast.broadcastTile(tile);
				ServerPlayer.this.broadcast.broadcastPlayer(player);
				return(tile);
			}
			catch(InteractException ex)
			{
				sendException(ex);
			}
			return(null);
		}
		@SuppressWarnings("unused")
		public void initGame(String name,String description)
		{
			ServerPlayer.this.player = game.newPlayer(name, description, new Point(),0);
			RPC.sendCall(ServerPlayer.this.socket, "setGameLogic", game);
			ServerPlayer.this.broadcast.broadcastPlayer(player);
		}
	}
}
