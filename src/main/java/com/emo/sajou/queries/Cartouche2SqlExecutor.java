package com.emo.sajou.queries;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.query.annotations.QueryClass;
import com.emo.mango.spring.query.annotations.Sql;
import com.emo.mango.spring.query.support.SqlExecutor;
import com.emo.sajou.queries.Cartouche2SqlExecutor.SearchCartouche;

@Component
@Sql(sql = "from Cartouche c where c.compte = :compte", clazz=CartoucheSql.class, name="Cartouche2Sql")
@QueryClass(SearchCartouche.class)
public class Cartouche2SqlExecutor extends SqlExecutor<CartoucheSql> {
	
	public static class SearchCartouche {
		
		public final String compte;
		
		public SearchCartouche(final String compte) {
			this.compte = compte;
		}
		
	}
	
	@Inject
	public Cartouche2SqlExecutor(final DataSource ds) {
		super(ds);
	}
}
