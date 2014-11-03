package networking;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import networking.SocketThread.SocketHandler;
import org.junit.Test;


class Counter
{
	int a = 0;
	public void increment(Integer amount)
	{
		a = a + amount;
	}
	public int get()
	{
		return(a);
	}
}
public class AllTests {

	@Test
	public void testRPC() {

		Counter number = new Counter();
		RPC test = new RPC("increment",2);
		test.execute(number);

		assertTrue(number.get() == 2);
	}
	@Test
	public void testNetworkRCP()
	{


		byte[] test = RPC.createCall("increment", 2); //Create a call and encode it to be sent over the network
		Counter c = new Counter(); //Create an object to apply the call
		RPC.reCreate(test).execute(c).execute(c); //Decode the call and apply it to the object twice

		assertTrue(c.get() == 4);


	}


	private Object callback;
	@Test
	public void testLocalNetowkedRCP() throws IOException
	{
		(new Thread()
		{
			public void run()
			{
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Socket clientSocket = new Socket("127.0.0.1", 3333);
					if(!clientSocket.isConnected())
						assert(false);
					RPC.sendCall(clientSocket, "success");
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();


		callback = new Object()
		{
			@SuppressWarnings("unused")
			public void success()
			{
				System.out.println("Heere");
				assertTrue(true);
			}
		};


		@SuppressWarnings("resource")
		ServerSocket ssocket = new ServerSocket(3333);
		new SocketThread(ssocket.accept(), new SocketHandler() {
			@Override
			public void dataReady(byte[] input, OutputStream outputStream) {
				System.out.println("Data Ready");
				RPC.reCreate(input).execute(AllTests.this.callback);
			}

			@Override
			public void disconnect() {}
		});



	}
}
