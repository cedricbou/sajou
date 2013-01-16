package com.emo.sajou.domain.commons;

public class SoldePourUsage {
	public final Usage usage;
	public final Solde solde;
	
	public SoldePourUsage(final Usage usage, final Solde solde) {
		this.usage = usage;
		this.solde = solde;
	}
	
	// TODO : equals, hashcode, ...
	
	@Override
	public String toString() {
		return usage + " : " + solde;
	}
}
