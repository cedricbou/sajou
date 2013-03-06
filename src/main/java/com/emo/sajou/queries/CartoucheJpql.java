package com.emo.sajou.queries;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.emo.mango.cqs.queries.annotations.QueryMap;

public class CartoucheJpql {
	public final String creation;
	public final String usage;
	public final String id;
	public final String compte;
	public final long solde;
	public final String validite;

	public CartoucheJpql(@QueryMap("c.creationDate") DateTime creation,
		@QueryMap("c.id.id") String id, 
		@QueryMap("c.usage.servicesAsString") String usage, 
		@QueryMap("c.numeroCompte.compte") String compte, 
		@QueryMap("c.solde.solde") long solde,
		@QueryMap("c.validite") LocalDate validite) {
		
		this.id = id;
		this.usage = usage;
		this.compte = compte;
		this.creation = creation.toString(DateTimeFormat.shortDateTime());
		this.validite = validite.toString(DateTimeFormat.shortDate());
		this.solde = solde;
	}
}
