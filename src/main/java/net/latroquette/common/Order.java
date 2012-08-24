package net.latroquette.common;

import java.io.Serializable;
import java.net.SocketAddress;

import net.latroquette.common.utils.optimizer.CommonValues;




public class Order implements  Serializable{

	/**
	 * 1 - First version, not tested
	 */
	private static final long serialVersionUID = 1L;
	private byte orderType;
	private boolean async;
	private Serializable instruction;
	public static final Order ERROR = new Order(OrderType.ERROR, null, true);
	public static final Order ACK = new Order(OrderType.ACK, null, true);
	public static final int NO_ORDER_SEQUENCE = CommonValues.ERROR_OR_INFINITE;
	private static int SESSION_ID = CommonValues.ERROR_OR_INFINITE;
	private boolean requireAnswer;
	private SocketAddress socketAddress;
	private int orderSequence;
	private Order parentOrder;
	private int sessionId;
	
		
	public Order(OrderType orderType, Serializable instruction, boolean async)
	{
		this.orderType = orderType.getValue();
		this.instruction = instruction;
		this.async = async;
		this.requireAnswer = !this.isAckOrder() && !this.isErrorOrder(); 
		this.orderSequence = NO_ORDER_SEQUENCE;
		this.parentOrder = this;
		this.sessionId = SESSION_ID;
	}
	
	public static void setSessionID(int sessionId) { SESSION_ID = sessionId;}
	
	public void setParentOrder(Order o)
	{
		this.parentOrder = o ;
	}
	
	public boolean hasParentOrder(Order o)
	{
		return parentOrder != this;
	}
	
	public Order getParentOrder()
	{
		return this.parentOrder;
	}
	
	public Order(OrderType orderType, Serializable instruction)
	{
		this(orderType, instruction, false);
	}
	
	/**
	 * @param instruction the instruction to set
	 */
	public void setInstruction(Serializable instruction) {
		this.instruction = instruction;
	}
	/**
	 * @return the instruction
	 */
	public Serializable getInstruction() {
		return instruction;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType.getValue();
	}
	/**
	 * @return the OrderType object
	 */
	public OrderType getOrderType() {
		for(OrderType o : OrderType.values())
		{
			if(o.getValue() == orderType)
				return o;
		}
		return null;
	}

	/**
	 * @return if request is asynchronous or not
	 */
	public boolean isAsynchronous()
	{
		return async;
	}
	
	public String toString()
	{
		return this.getOrderType() + ": " + instruction; 
	}
	
	public boolean isAckOrder()
	{
		return this.orderType == OrderType.ACK.getValue();
	}
	
	public boolean isErrorOrder()
	{
		return this.orderType == OrderType.ERROR.getValue();
	}
	
	public boolean requireAnswer()
	{
		return this.requireAnswer ;
	}
	
	public void setSocketAddress(SocketAddress ipClient)
	{
		this.socketAddress =  ipClient;
	}
	
	public SocketAddress getSocketAddress()
	{
		return this.socketAddress ;
	}
	
	public void setOrderSequence(int i)
	{
		this.orderSequence = i;
	}
	
	public int getOrderSequence()
	{
		return this.orderSequence;
	}
	
	public boolean hasOrderSequence()
	{
		return this.getOrderSequence() != NO_ORDER_SEQUENCE;
	}
	
	public int getSessionId()
	{
		return this.sessionId;
	}
	
	public void setSessionIdFrom(Order o)
	{
		this.sessionId = o.getSessionId(); 
	}
}
