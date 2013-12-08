package net.latroquette.rest.forum;

public enum SMFMethods {
	/**
	 * Need a {@code "member_name"} parameter with {@code login}, 
	 * a {@code "email"} parameter with {@code mail},
	 * a {@code "password"} parameter with clear {@code password} at least 
	 * Others parameter are usable regarding the SMF register_member method
	 */
	SMF_REGISTER_ENDPOINT("/register/member.json"),
	/**
	 * Need only a {@code "member"} parameter with {@code smfId} 
	 */
	SMF_ACTIVATE_ENDPOINT("/activate/member.json"),
	/**
	 * Need a {@code "member"} parameter with {@code smfId} 
	 * and a {@code "password"} parameter with clear password 
	 */
	SMF_PASSWORD_ENDPOINT("/change/password.json");
	
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
