package com.emo.sajou.queries;

import java.util.List;

import com.emo.mango.cqs.Queries;
import com.emo.mango.cqs.queries.annotations.MangoJpql;
import com.emo.mango.cqs.queries.annotations.MangoSql;
import com.emo.mango.cqs.queries.annotations.QueryBind;

public interface CartoucheQueries extends Queries {

	@MangoJpql("from Cartouche c where c.numeroCompte.compte = :compte")
	public List<CartoucheJpql> findCartoucheByCompte(
			@QueryBind("compte") String compte);

	@MangoSql("from Cartouche c where c.compte = :compte and c.solde >= :solde")
	public List<CartoucheSql> findCartoucheByCompteAndSoldeAbove(
			@QueryBind("compte") String numeroCompte,
			@QueryBind("solde") int solde);
}
