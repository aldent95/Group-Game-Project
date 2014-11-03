package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This is a framework class that adds abstraction to Java Sockets. It takes a connected socket and a callback.
 * When data is ready it will read a content length and then read that much data. Once complete it will call the specified callback.
 * This is not thread safe but it does not pose a real problem in this context.
 * @author Michael Pearson
 */
public class SocketThread extends Thread{

	public interface SocketHandler {
		/**
		 * This method will be called when data is ready from the server.
		 * @param input
		 * @param outputStream
		 */
		void dataReady(byte[] input,OutputStream outputStream);
		/**
		 * This will be called if the socket is disconnected
		 */
		void disconnect();
	}
	Socket socket;
	SocketHandler callback;
	InputStream inputStream;
	OutputStream outputStream;

	public SocketThread(Socket socket,SocketHandler callback) {
		this.socket = socket;
		this.callback = callback;

		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(!socket.isClosed() && socket.isConnected())
		{
			try {
				int data[] = new int[2];
				if((data[0] = inputStream.read()) != -1)
				{
					data[1] =  inputStream.read();
					int contentLength = ((data[0] & 0xFF) << 8) | (data[1] & 0xFF);
					if(contentLength > 0)
					{
						byte buffer[] = new byte[contentLength];
						int totalRead = 0;
						while(totalRead < contentLength) //Risky but the easiest way to ensure that the entire message gets through.
						{
							totalRead += inputStream.read(buffer, totalRead, contentLength - totalRead);
						}
						callback.dataReady(buffer, outputStream);
					}
					else
					{
						throw new IOException("Malformed packet!\n");
					}
				}
				else
				{
					socket.close();
				}
			} catch (IOException e) {
				if(socket.isConnected() && !socket.isClosed())
				{
					e.printStackTrace();
				}
				break;
			}
		}
		disconnected();
	}
	private void disconnected()
	{
		callback.disconnect();
	}
}
