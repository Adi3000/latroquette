package net.latroquette.rest.forum;

public enum SMFMethods {
	SMF_REGISTER_ENDPOINT("/register/member.json");
	
	private String path; 
	private SMFMethods(String path) {
		this.path = path;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	public String toString(){
		return getPath();
	}
}
