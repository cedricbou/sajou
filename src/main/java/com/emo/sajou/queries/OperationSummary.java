package com.emo.sajou.queries;

import com.emo.mango.cqs.queries.annotations.QueryMap;

public class OperationSummary {
	public final String creation;
	public final String usage;
	public final Long montant;

	public OperationSummary(@QueryMap("o.creationDate") String creation,
		@QueryMap("o.servicesAsString") String usage, 
		@QueryMap("o.montant") Long montant) {
		
		this.usage = usage;
		this.creation = creation;
		this.montant = montant;
	}

}
