package net.latroquette.common.database.data.file;

public enum GarbageFileStatus {
	
	VALIDATE(0),
	NOT_LINKED(1),
	FORCE_DELETE(2),
	ERROR_ON_DELETE(3);

	private int value;
	private GarbageFileStatus(int id){
		this.value = id;
	}
	
	public Integer getValue(){
		return value;
	}

	public static GarbageFileStatus valueOf(Integer value) {
	    for (GarbageFileStatus garbageFileStatus : GarbageFileStatus.values()) {
	        if (garbageFileStatus.getValue().equals(value)) {
	            return garbageFileStatus;
	        }
	    }
	    return null;
	}
	
}
