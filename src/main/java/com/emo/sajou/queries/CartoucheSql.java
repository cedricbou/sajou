package com.emo.sajou.queries;

import com.emo.mango.cqs.queries.annotations.QueryMap;

public class CartoucheSql {
	public final String creation;
	public final String usage;
	public final String id;
	public final String compte;
	public final Long solde;
	public final String validite;

	public CartoucheSql(@QueryMap("c.creationDate") String creation,
		@QueryMap("c.cartoucheId") String id, 
		@QueryMap("c.usage_servicesAsString") String usage, 
		@QueryMap("c.numerocompte_compte") String compte, 
		@QueryMap("c.solde_solde") Long solde,
		@QueryMap("c.validite") String validite) {
		
		this.id = id;
		this.usage = usage;
		this.compte = compte;
		this.creation = creation;
		this.validite = validite;
		this.solde = solde;
	}
}
