package networking;

import gameLogic.GameLogic;
import gameLogic.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.ServerPlayer.BroadcastAdaptor;
import storage.Saver;
/**
 * The GameServer class is the networking frontend of the server. It opens the socket for listening and then spawns child threads for each client connection.
 * @author Michael Pearson
 */
public class GameServer implements BroadcastAdaptor{
	private NetworkStatusProxy statusTextProxy;
	private GameLogic game;
	private List<ServerPlayer> clients;
	private ServerSocket socket;
	private Thread listeningThread;


	public interface NetworkStatusProxy
	{
		public void sendMessage(String message);
		public void clientDisconnect(ServerPlayer client);

	}

	private void printf(String line,Object... args){
		statusTextProxy.sendMessage(String.format(line, args));
	}

	public GameServer(NetworkStatusProxy statusTextProxy,int port,GameLogic game) {
		port = (port > 0) ? port : 3333;
		this.statusTextProxy = statusTextProxy;
		this.game = game;
		beginListening(port);
	}


	private void beginListening(int port)
	{
		clients = new ArrayList<ServerPlayer>();
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			printf("Could not open socket for listening.\n");
			e.printStackTrace();
		}
		(listeningThread = new Thread() {
			@Override
			public void run() {
				while(!interrupted())
				{
					try {
						GameServer.this.handel(GameServer.this.getSocket().accept());
					} catch (IOException e) {} //Fail gracefully. Usually get here because socket.close() was called
				}
				try {
					GameServer.this.getSocket().close();
				} catch (IOException e) {
					System.err.println("Could not close listening socket");
				}
			}
		}).start();
		printf("Listening for connections on %s:%d\n",socket.getInetAddress().getHostAddress(),port);
		printf("Ready to accept connections\n");
	}

	private ServerSocket getSocket()
	{
		return(socket);
	}
	public void shutdown()
	{
		listeningThread.interrupt();
		for(int a = 0;a < clients.size();a++)
		{
			ServerPlayer client = clients.get(a);
			try
			{
				if(client != null)
				{
					client.disconnect();
				}
			}
			catch (Exception e){}
		}
		printf("Shutting down server\n");
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close socket");
		}

		printf("Saving\n");
		Saver.saveGameLogic(game, Saver.DEFAULT_SAVE_FILENAME);
	}
	protected void handel(Socket socket) {
		printf("Accepting connection from %s\n",socket.getInetAddress().getHostAddress());

		ServerPlayer player = new ServerPlayer(socket, game,new NetworkStatusProxy() {
			public void sendMessage(String message) {GameServer.this.printf(message);}
			public void clientDisconnect(ServerPlayer client) {GameServer.this.clients.remove(client);}
		},this);

		(new SocketThread(socket, player)).start();

		clients.add(player);
	}
	public void broadcastPlayer(Player player)
	{
		for(ServerPlayer client : clients)
		{
			client.sendPlayerToClient(player);
		}
	}
	public void broadcastTile(TransmitTile tile)
	{
		for(ServerPlayer client : clients)
		{
			client.sendTileToClient(tile);
		}
	}
}
