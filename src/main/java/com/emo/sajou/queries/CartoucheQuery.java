package com.emo.sajou.queries;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.emo.mango.spring.jpa.annotations.Query;

public class CartoucheQuery {
	public final String creation;
	public final String usage;
	public final String id;
	public final String compte;
	public final long solde;
	public final String validite;

	public CartoucheQuery(@Query("c.creationDate") DateTime creation,
		@Query("c.id.id") String id, 
		@Query("c.usage.servicesAsString") String usage, 
		@Query("c.numeroCompte.compte") String compte, 
		@Query("c.solde.solde") long solde,
		@Query("c.validite") LocalDate validite) {
		
		this.id = id;
		this.usage = usage;
		this.compte = compte;
		this.creation = creation.toString(DateTimeFormat.shortDateTime());
		this.validite = validite.toString(DateTimeFormat.shortDate());
		this.solde = solde;
	}
}
