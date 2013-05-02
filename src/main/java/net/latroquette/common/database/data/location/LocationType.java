package net.latroquette.common.database.data.location;


public enum LocationType {
	TOWN(1, "Town"),
	DEPARTMENT(2, "Department"),
	PROVINCE(3, "Province"),
	COUNTRY(4, "Country");
	private Integer value;
	private String label;
	
	private LocationType(Integer id, String label){
		this.value = id;
		this.label = label;
	}
	public static LocationType valueOf(Integer id) {
        for (LocationType item : LocationType.values()) {
            if (item.getValue().equals(id)) {
                return item;
            }
        }
        return null;
    }
	public Integer getValue(){
		return value;
	}

	
	public String getLabel(){
		return label;
	}
	public String toString(){
		return value.toString();
	}
	
	
}
