package com.emo.sajou.queries;

import java.util.List;

import com.emo.mango.cqs.Queries;
import com.emo.mango.cqs.queries.annotations.MangoSql;
import com.emo.mango.cqs.queries.annotations.QueryBind;

public interface OperationQueries extends Queries {

	@MangoSql(value="from operation o where o.compte = :compte order by o.creationDate desc")
	public List<OperationSummary> allOperationsForCompte(final @QueryBind("compte") String compte);
}
