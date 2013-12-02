package net.latroquette.common.database.data.profile;

import java.io.Serializable;
import java.util.Date;

public class XMPPSession implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227952255846637309L;
	private final String jid; 
	private final String sid; 
	private final Long rid;
	private final Date sessionDate;
	public XMPPSession(final String jid, final String sid, final Long rid) {
		this.jid = jid;
		this.rid = rid;
		this.sid = sid;
		this.sessionDate = new Date();
	}
	public String getJid() {
		return jid;
	}
	public String getSid() {
		return sid;
	}
	public Long getRid() {
		return rid;
	}
	public Date getSessionDate() {
		return sessionDate;
	}
	public boolean isValid(){
		return jid != null && sid != null && rid != null;
	}
}
