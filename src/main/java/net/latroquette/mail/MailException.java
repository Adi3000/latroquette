package net.latroquette.mail;

public class MailException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8537256871866097103L;
	
	
	public MailException() {
		super();
	}

	public MailException(String message, Throwable e){
		super(message, e);
	}
	public MailException(String message){
		super(message);
	}
	public MailException(Throwable e){
		super(e);
	}
}
