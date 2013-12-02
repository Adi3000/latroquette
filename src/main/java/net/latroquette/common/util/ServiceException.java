package net.latroquette.common.util;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6522713669975225881L;
	
	public ServiceException(String message, Throwable e){
		super(message, e);
	}
	public ServiceException(String message){
		super(message);
	}
	public ServiceException(Throwable e){
		super(e);
	}
}
