package com.emo.sajou.domain.compte;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumeroCompte {
	@Column
	private /* should be final */ String compte;

	public NumeroCompte() {
		compte = UUID.randomUUID().toString();
	}

	public String getCompte() {
		return compte;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof NumeroCompte
				&& this.compte.equals(((NumeroCompte) obj).compte);
	}
	
	@Override
	public int hashCode() {
		return new Long(10 + compte.hashCode()).hashCode();
	}

	@Override
	public String toString() {
		return "compte-" + compte.toString();
	}	
}
