package net.latroquette.common.util.database.hibernate;

import org.hibernate.dialect.PostgreSQL82Dialect;

public class PostgresEnhancedSQLDialect extends PostgreSQL82Dialect {
	public PostgresEnhancedSQLDialect() {
		super();
		registerFunction("fulltextsearch", new PostgreSQLFullTextSearchFunction());
	}
}
