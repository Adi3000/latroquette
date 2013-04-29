package net.latroquette.common.database.data.item;

public enum ItemStatus {
	DRAFT(0),
	CREATED(1),
	APPROUVED(2),
	ACTIVATED(3),
	DESACTIVATED(4),
	EXPIRED(5),
	FINISHED(6);
	
	private int value;
	private ItemStatus(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
}
