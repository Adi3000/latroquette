package net.latroquette.common.database.data.keyword;

public enum KeywordType {
	
	MAIN_KEYWORD(1),
	SYNONYME_KEYWORD(2),
	EXTERNAL_KEYWORD(3);
	
	private final int id;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	private KeywordType(int id){
		this.id = id;
	}
	
	public static KeywordType get(int type) {
	    for (KeywordType e : values()) {
	        if (e.id == type) {
	            return e;
	        }
	    }
	    return null;
	}
	
	public String toString(){
		return String.valueOf(id);
	}
}
