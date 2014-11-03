package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
/**
 * RCP (Remote procedure call) is the low level networking inplementation. This class simply bundles commands into a "pack" and transmits them.
 * @author Michael Pearson
 */
@SuppressWarnings("serial")
public class RPC implements Serializable{

	private String functionName;
	private Serializable[] arguments;
	/**
	 * Takes the name and arguments of the function this object should call on the remote computer.
	 * @param functionName
	 * @param arguments
	 */
	RPC(String functionName,Serializable... arguments) {
		this.functionName = functionName;
		this.arguments = arguments;
	}
	/**
	 * Executes functionName(arguments[0],arguments[1],arguments[n]) on obj
	 * @param obj
	 * @return this RPC object
	 */
	public RPC execute(Object obj)
	{
		@SuppressWarnings("rawtypes")
		Method method;


		if(functionName == null)
			return(this);

		Class[] parms = new Class[arguments.length];
		int i = 0;
		for(Object argument : arguments) //We need the class of all of the arguments to find which method to call.
		{
			if(argument != null) //If one of the arguments are null avoid the exception here and fingers crossed whoever receives it can handle it.
			{
				parms[i++] = argument.getClass();
			}
			else
			{
				parms[i++] = null;
			}
		}
		try {
			method = getMethod(obj, functionName, parms); //Custom get method because we can handle nulls
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Could not find method, %s for RPC. Looking for %d arguments",functionName,parms.length)); //Ohhh nooooo
		}
		try {
			method.invoke((Object)obj,(Object[])arguments);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException("Could not invoke RPC object"); //Something went wrong :/ not too sure. Hasnt been a problem yet.
		}catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace(); //In this case it totally wasnt this code that broke!
		}
		return(this);
	}

	/**
	 * This is a custom implementation of getMethod because the built in one really dosnt like null class types. This makes sense because usally reflection requires all of the classes of your function call to ensure the correct function is called. For this project this can be ignored.
	 * @param obj the object we want to find a method in.
	 * @param functionName The name of the function in the call and that we are looking for in obj
	 * @param parms the arguments!
	 * @return The method "functionName()" in obj
	 * @throws NoSuchMethodException This is pretty easy to get.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Method getMethod(Object obj,String functionName,Class[] parms) throws NoSuchMethodException
	{

		Method[] methods = obj.getClass().getMethods();
		for(Method m : methods)
		{
			if(m.getName().equals(functionName))
			{
				Class[] arguments = m.getParameterTypes();

				if(arguments.length != parms.length)
					continue;
				boolean found = true;
				for(int a = 0;a<arguments.length;a++)
				{
					if(parms[a] == null)
					{
						continue;
					}
					if(!arguments[a].isAssignableFrom(parms[a]))
					{
						found = false;
						break;
					}
				}
				if(found)
				{
					return(m);
				}
			}
		}
		throw new NoSuchMethodException();
	}
	/**
	 * so this method takes a functionName and some Serializable arguments. It packages them up in a convinent byte array.
	 * @param functionName
	 * @param arguments
	 * @return the call in a byte[]
	 */
	public static byte[] createCall(String functionName, Serializable... arguments)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream output = new ObjectOutputStream(stream);
			output.writeObject(new RPC(functionName, arguments));
			return(stream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return(null);
		}
	}
	/**
	 * This method unpackages the data from createCall and created an RPC object.
	 * @param data
	 * @return
	 */
	public static RPC reCreate(byte[] data)
	{
		try {
			ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(data));
			RPC obj = (RPC)input.readObject();
			return(obj);
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
			return(new RPC(null));
		}
	}
	/**
	 * this is just a handy way of creating and sending an rpc object all in one.
	 * @param socket the socket to send the call on
	 * @param functionName the name of the function to be called
	 * @param arguments the arguments to call functionName on
	 */
	public static synchronized void sendCall(Socket socket,String functionName,Serializable... arguments)
	{
		try {
			OutputStream stream = socket.getOutputStream();
			byte call[] = RPC.createCall(functionName, arguments);
			int contentLength = call.length;
			if(contentLength > 65534){throw new IOException("Content Too Large");}
			byte data[] = new byte[2];
			data[0] = (byte) ((contentLength & 0xFF00) >> 8);
			data[1] = (byte) ((contentLength & 0x00FF) >> 0);
			stream.write(data);
			stream.write(call);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
