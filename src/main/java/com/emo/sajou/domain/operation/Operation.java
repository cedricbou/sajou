package com.emo.sajou.domain.operation;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.emo.mango.annotations.Final;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

@Entity
public class Operation {
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationDate = DateTime.now();

	@Embedded
	private @Final OperationId id;

	@Embedded
	private @Final Usage usage;

	@Embedded
	private @Final NumeroCompte numeroCompte;
	
	@Column(nullable = false)
	private @Final long montant;

	public Operation(final NumeroCompte numeroCompte, final Usage usage, final long montant) {
		this.id = new OperationId();
		this.usage = usage;
		this.numeroCompte = numeroCompte;
		this.montant = montant;
	}

	public OperationId getId() {
		return id;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public Usage getUsage() {
		return usage;
	}

	public NumeroCompte getNumeroCompte() {
		return numeroCompte;
	}

	public long getMontant() {
		return montant;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Operation
				&& usage.equals(((Operation) obj).usage)
				&& montant == (((Operation) obj).montant)
				&& numeroCompte.equals(((Operation) obj).numeroCompte);
	}
	
	@Override
	public String toString() {
		return "{id: " + id + ", compte: " + numeroCompte + ", montant: " + montant
				+ ", usage: " + usage + "}";
	}

	// For JPA use.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eid;

	@Deprecated
	protected Long getEid() {
		return eid;
	}

	@Deprecated
	protected Operation() {

	}
}
