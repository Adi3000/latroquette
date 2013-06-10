package net.latroquette.common.util.optimizer;

public enum DataType {

	STRING("s1"),
	STRING_LIST("sL"),
	INTEGER("n1"),
	INTEGER_LIST("nL");
	
	private String id;
	private DataType(String id){
		this.id = id;
	}
	
	public static DataType getDataType(String id){
		DataType dtFound = null;
		for(DataType dt : values()){
			if(dt.id.equals(id)){
				dtFound = dt;
				break;
			}
		}
		return dtFound;
	}
	
	public String getId(){
		return id;
	}
}
