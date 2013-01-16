package com.emo.sajou.domain.utilisation;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.emo.sajou.domain.cartouche.CartoucheId;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

@Entity
public class Utilisation {

	@Column(nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationDate = DateTime.now();

	@Embedded
	private /* should be final */ NumeroCompte numeroCompte;
	
	@Embedded
	private /* should be final */ CartoucheId cartoucheId;
	
	@Embedded
	private /* should be final */ Usage usage;
	
	@Column(nullable=false)
	private /* should be final */ long montant;

	public Utilisation(final NumeroCompte numeroCompte,
			final CartoucheId cartoucheId, final long montant, final Usage usage) {
		this.numeroCompte = numeroCompte;
		this.cartoucheId = cartoucheId;
		this.usage = usage;
		this.montant = montant;
	}

	public NumeroCompte getNumeroCompte() {
		return numeroCompte;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public CartoucheId getCartoucheId() {
		return cartoucheId;
	}

	public Usage getUsage() {
		return usage;
	}

	public long getMontant() {
		return montant;
	}

	@Override
	public int hashCode() {
		return new Long(numeroCompte.hashCode() + cartoucheId.hashCode() + usage.hashCode() + montant).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Utilisation
			&& numeroCompte.equals(((Utilisation)obj).numeroCompte)
			&& cartoucheId.equals(((Utilisation)obj).cartoucheId)
			&& usage.equals(((Utilisation)obj).usage)
			&& montant == (((Utilisation)obj).montant);
	}
	
	// For JPA use.
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long eid;
	
	@Deprecated
	protected Long getEid() {
		return eid;
	}
	
	@Deprecated
	protected Utilisation() {
		
	}

}
