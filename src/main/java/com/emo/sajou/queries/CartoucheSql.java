package com.emo.sajou.queries;

import com.emo.mango.spring.query.annotations.Query;

public class CartoucheSql {
	public final String creation;
	public final String usage;
	public final String id;
	public final String compte;
	public final Long solde;
	public final String validite;

	public CartoucheSql(@Query("c.creationDate") String creation,
		@Query("c.cartoucheId") String id, 
		@Query("c.servicesAsString") String usage, 
		@Query("c.compte") String compte, 
		@Query("c.solde") Long solde,
		@Query("c.validite") String validite) {
		
		this.id = id;
		this.usage = usage;
		this.compte = compte;
		this.creation = creation;
		this.validite = validite;
		this.solde = solde;
	}
}
