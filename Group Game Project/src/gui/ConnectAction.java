package gui;
/**
 * 
 * 
 * 
 * @author Michael Pearson
 *
 */
public interface ConnectAction
{
	public void connect(String ip,int port,String name,String description);
	public void startServer(int port);
}
