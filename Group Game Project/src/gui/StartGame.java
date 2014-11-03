package gui;


/**
 *
 * This is responsible for starting the game
 *
 * it takes either 3 or 2 arguments depending on whether or not you want to start a server
 * or client
 * @author bookerandr
 *
 */
public class StartGame {

	public static void main(String[] args) {

		// ip, port
		@SuppressWarnings("unused")
		StartGame  s = new StartGame(args);



	}
/**
 *
 * this take in the arguments
 *
 * @param args
 */
	private StartGame(String[] args) {

		if (args.length == 2 && args[0].equals("server")){
				int port = Integer.parseInt(args[1]);
				@SuppressWarnings("unused")
				StartServer s = new StartServer(port);
		}
		else if(args.length == 4 && args[0].equals("client"))
		{
			StartClient client = new StartClient(args[1],Integer.valueOf(args[1]), args[2], args[3]);
		}
		else
		{
			StartClient client = new StartClient();
		}
	}

}
