package net.latroquette.common;

import java.io.Serializable;

public enum OrderType implements Serializable{
	
	ERROR ("Error", (byte) 7),
	ACK ("Acquitment", (byte) 1),
	INFO ("Information", (byte) 5),
	CONNECT ("Connection", (byte) 2),
	ECHO ("Echo" ,(byte) 4),
	UPDATE("Updating",(byte) 0),
	
	/* Level 2 Order */
	
	AUTH("Authentication", (byte)8);

	private transient String name;
	private byte value;
	OrderType (String name, byte value)
	{
		this.name = name;
		this.value = value;
	}

	public byte getValue () {return this.value;}
	
	public String toString (){
		return "["+value+"] "+ name;
	}

}

