package net.latroquette.common.database.data.location;


public enum LocationType {
	TOWN(1, "Town"),
	DEPARTMENT(2, "Department"),
	PROVINCE(3, "Province"),
	COUNTRY(4, "Country");
	private Integer id;
	private String label;
	
	private LocationType(Integer id, String label){
		this.id = id;
		this.label = label;
	}
	public static LocationType valueOf(Integer id) {
        for (LocationType item : LocationType.values()) {
            if (item.getId().intValue() == id.intValue()) {
                return item;
            }
        }
        return null;
    }
	public Integer getId(){
		return id;
	}

	
	public String getLabel(){
		return label;
	}
	public String toString(){
		return id.toString();
	}
	
	
}
