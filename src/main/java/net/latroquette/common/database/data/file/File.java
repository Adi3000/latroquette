package net.latroquette.common.database.data.file;


import java.sql.Timestamp;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.latroquette.common.database.data.AbstractDataObject;
import net.latroquette.common.database.data.profile.User;

@Entity
@Table(name = "files")
@XmlRootElement
@SequenceGenerator(name = "files_file_id_seq", sequenceName = "files_file_id_seq", allocationSize=1)
public class File extends AbstractDataObject{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1381954874430138843L;
	public static final String TABLE_AND_ENTITY_NAME = "files";

	private String name;
    private java.io.File file;
    private Integer id;
    private User owner;
    private String checksum;
    private Integer garbageStatus;
    private Timestamp uploadDate;
	/**
	 * @return the title
	 */
    @Column(name="file_name")
	public String getName() {
		return name;
	}
	/**
	 * @param title the title to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the file
	 */
	@Transient
	@XmlTransient
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
	@Id
	@Column(name="file_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_file_id_seq")
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
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="user_id")
	@XmlTransient
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
	@Column(name="file_checksum")
	@XmlTransient
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
	@Column(name="file_garbage_status")
	@XmlTransient
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
	 * @param garbageStatus the garbageStatus to set
	 */
	public void setGarbageStatus(GarbageFileStatus garbageStatus) {
		this.garbageStatus = garbageStatus.getValue();
	}
	/**
	 * @return the uploadDate
	 */
	@Column(name="file_upload_date")
	@XmlTransient
	public Timestamp getUploadDate() {
		return uploadDate;
	}
	/**
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}
}
