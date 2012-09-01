package net.latroquette.common.database.data.file;


import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.data.profile.User;

public class File extends AbstractDataObject{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1381954874430138843L;
	public static final String TABLE_AND_ENTITY_NAME = "Files";

	private String name;
    private java.io.File file;
    private Integer id;
    private User owner;
    private String checksum;
    private Integer garbageStatus;
    private FileType fileType;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return name;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.name = title;
	}
	/**
	 * @return the file
	 */
	public java.io.File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(java.io.File file) {
		this.file = file;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}
	/**
	 * @param checksum the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	/**
	 * @return the garbageStatus
	 */
	public Integer getGarbageStatus() {
		return garbageStatus;
	}
	/**
	 * @param garbageStatus the garbageStatus to set
	 */
	public void setGarbageStatus(Integer garbageStatus) {
		this.garbageStatus = garbageStatus;
	}	
	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

}
