package com.emo.sajou.queries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.query.annotations.QueryClass;
import com.emo.mango.spring.query.annotations.Sql;
import com.emo.mango.spring.query.support.SqlExecutor;
import com.emo.sajou.queries.Cartouche2SqlExecutor.SearchCartouche;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@Sql(sql = "from Cartouche c where c.compte in (:comptes)", clazz=CartoucheSql.class, name="Cartouche2Sql")
@QueryClass(SearchCartouche.class)
public class Cartouche2SqlExecutor extends SqlExecutor<CartoucheSql> {
	
	public static class SearchCartouche {
		
		public final Set<String> comptes;
		
		@JsonCreator
		public SearchCartouche(final @JsonProperty("comptes") Set<String> comptes) {
			this.comptes = comptes;
		}
		
		
		public SearchCartouche(final String... comptes) {
			this.comptes = new HashSet<String>(Arrays.asList(comptes));
		}
	}
	
	@Inject
	public Cartouche2SqlExecutor(final DataSource ds) {
		super(ds);
	}
}
