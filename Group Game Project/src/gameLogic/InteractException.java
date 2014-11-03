package gameLogic;

public class InteractException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1651121531551888745L;
	private final int code;
	private final static String[] message = {"Item not in room", "Object is not an item", "Cannot pickup item", "Inventory full"};
	public InteractException(int code){
		super(message[code]);
		this.code = code;
	}
	public int getCode(){
		return code;
	}

}
