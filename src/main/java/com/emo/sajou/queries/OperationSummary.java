package com.emo.sajou.queries;

import com.emo.mango.cqs.queries.annotations.QueryMap;

public class OperationSummary {
	public final String creation;
	public final String usage;
	public final String debit;
	public final String credit;

	public OperationSummary(@QueryMap("o.creationDate") String creation,
		@QueryMap("o.couleurs") String usage, 
		@QueryMap("o.debit") String debit, @QueryMap("o.credit") String credit) {
		
		this.usage = usage;
		this.creation = creation;
		this.debit = debit;
		this.credit = credit;
	}

}
