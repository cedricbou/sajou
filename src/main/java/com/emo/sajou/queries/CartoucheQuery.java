package com.emo.sajou.queries;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class CartoucheQuery {
	public final String creation;
	public final String usage;
	public final String id;
	public final String compte;
	public final long solde;
	public final String validite;
	
	public CartoucheQuery(DateTime creation, String id, String usage, String compte, long solde , LocalDate validite) {
		this.id = id;
		this.usage = usage;
		this.compte = compte;
		this.creation = creation.toString(DateTimeFormat.shortDateTime());
		this.validite = validite.toString(DateTimeFormat.shortDate());
		this.solde = solde;
	}
}
