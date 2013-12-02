package net.latroquette.rest.forum;

public class SMFRestException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4731342369222907689L;
	public SMFRestException(String message, Throwable e){
		super(message, e);
	}
	public SMFRestException(String message){
		super(message);
	}
	public SMFRestException(Throwable e){
		super(e);
	}
}
