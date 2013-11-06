package net.latroquette.common.database.data.place;


public enum PlaceType {
	TOWN(1, "Town"),
	DEPARTMENT(2, "Department"),
	PROVINCE(3, "Province"),
	COUNTRY(4, "Country");
	private Integer value;
	private String label;
	
	private PlaceType(Integer id, String label){
		this.value = id;
		this.label = label;
	}
	public static PlaceType valueOf(Integer id) {
        for (PlaceType item : PlaceType.values()) {
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
