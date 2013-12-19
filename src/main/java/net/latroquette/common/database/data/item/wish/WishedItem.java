package net.latroquette.common.database.data.item.wish;

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

import org.apache.commons.lang3.StringUtils;

import net.latroquette.common.database.data.keyword.ExternalKeyword;
import net.latroquette.common.database.data.keyword.MainKeyword;

import com.adi3000.common.database.hibernate.data.AbstractDataObject;

@Entity
@Table(name = "wishes")
@SequenceGenerator(name = "wishes_wish_id_seq", sequenceName = "wishes_wish_id_seq", allocationSize=1)
public class WishedItem extends AbstractDataObject implements Wish {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7501493373411705165L;
	public static final String WISHES_SOURCE = "wsh";
	private Integer id;
	private String source;
	private String name;
	private String uid;
	private ExternalKeyword externalKeyword;
	private MainKeyword mainKeyword;
	
	public WishedItem() {}

	@Override
	@Column(name="wish_name")
	public String getName() {
		return name;
	}

	@Override
	@Column(name="source_id")
	public String getSource() {
		return source;
	}

	@Override
	@Id
	@Column(name="wish_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishes_wish_id_seq")
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/**
	 * @return the uid
	 */
	@Column(name="ext_uid")
	public String getUid() {
		return StringUtils.isEmpty(uid) ? getId().toString() : uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the externalKeyword
	 */
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="ext_keyword_id")
	public ExternalKeyword getExternalKeyword() {
		return externalKeyword;
	}

	/**
	 * @param externalKeyword the externalKeyword to set
	 */
	public void setExternalKeyword(ExternalKeyword externalKeyword) {
		this.externalKeyword = externalKeyword;
	}

	/**
	 * @return the mainKeyword
	 */
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="keyword_id")
	public MainKeyword getMainKeyword() {
		return mainKeyword;
	}

	/**
	 * @param mainKeyword the mainKeyword to set
	 */
	public void setMainKeyword(MainKeyword mainKeyword) {
		this.mainKeyword = mainKeyword;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		return this.equals((Wish)o);
	}
	public boolean equals(Wish wish){
		return this.getSource().equals(wish.getSource()) &&
				this.getUid().equals(wish.getUid());
	}
}
