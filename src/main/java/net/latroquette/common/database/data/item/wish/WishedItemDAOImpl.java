package net.latroquette.common.database.data.item.wish;

import org.springframework.stereotype.Repository;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

@Repository
public class WishedItemDAOImpl extends AbstractDAO<WishedItem> implements WishedItemDAO {

	public WishedItemDAOImpl() {
		super(WishedItem.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1473399410410471127L;
	
}
