package com.emo.sajou.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Solde {

	@Column(nullable=false)
	private /* should be final */ long solde;
	
	public Solde(final long solde) {
		this.solde = solde;
	}
	
	public long getSolde() {
		return solde;
	}
	
	public Solde deduire(final long montant) {
		return new Solde(solde - montant);
	}
	
	public Solde ajouter(final long montant) {
		return new Solde(solde + montant);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Solde
			&& solde == ((Solde)obj).solde;
	}
	
	@Override
	public int hashCode() {
		return new Long(13 + solde).hashCode();
	}
	
	@Override
	public String toString() {
		return new Long(solde).toString().trim();
	}
	
	@Deprecated
	protected Solde() {
		
	}
}
