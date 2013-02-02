package com.emo.sajou.queries;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.query.annotations.QueryParams;
import com.emo.mango.spring.query.annotations.Sql;
import com.emo.mango.spring.query.support.SqlExecutor;

@Component
@Sql(sql = "from Cartouche c where c.compte = ?", clazz=CartoucheSql.class)
@QueryParams("compte")
public class CartoucheSqlExecutor extends SqlExecutor<CartoucheSql> {
	
	@Inject
	public CartoucheSqlExecutor(final DataSource ds) {
		super(ds);
	}
}
