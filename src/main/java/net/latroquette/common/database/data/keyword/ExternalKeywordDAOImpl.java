package net.latroquette.common.database.data.keyword;

import org.springframework.stereotype.Repository;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

@Repository
public class ExternalKeywordDAOImpl extends AbstractDAO<ExternalKeyword> implements ExternalKeywordDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3927698500326011982L;

	public ExternalKeywordDAOImpl() {
		super(ExternalKeyword.class);
	}

}
