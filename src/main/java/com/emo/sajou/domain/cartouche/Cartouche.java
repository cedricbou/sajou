package com.emo.sajou.domain.cartouche;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.emo.sajou.domain.commons.Solde;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.utilisation.Utilisation;

@Entity
public class Cartouche implements Comparable<Cartouche> {

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationDate = DateTime.now();

	@Embedded
	private/* should be final */CartoucheId id;

	@Embedded
	private/* should be final */Usage usage;

	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private/* should be final */LocalDate validite;

	@Embedded
	private/* should be final */NumeroCompte numeroCompte;

	@Embedded
	private Solde solde;

	public Cartouche(final NumeroCompte numeroCompte, final Usage usage,
			final Period validite, final long montantInitial) {
		this.id = new CartoucheId();
		this.usage = usage;
		this.validite = LocalDate.now().plus(validite);
		this.numeroCompte = numeroCompte;
		this.solde = new Solde(montantInitial);
	}

	public CartoucheId getId() {
		return id;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public Usage getUsage() {
		return usage;
	}

	public LocalDate getValidite() {
		return validite;
	}

	public NumeroCompte getNumeroCompte() {
		return numeroCompte;
	}

	public Solde getSolde() {
		return solde;
	}

	public void deduire(final long montant) {
		solde = solde.deduire(montant);
	}

	public void ajouter(final long montant) {
		solde = solde.ajouter(montant);
	}

	public boolean accepte(final Usage usage) {
		return usage.isSubsetOf(this.usage);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Cartouche
				&& usage.equals(((Cartouche) obj).usage)
				&& validite.equals(((Cartouche) obj).validite)
				&& solde.equals(((Cartouche) obj).solde)
				&& numeroCompte.equals(((Cartouche) obj).numeroCompte);
	}

	public boolean expired() {
		return validite.isBefore(LocalDate.now())
				&& !LocalDate.now().isEqual(validite);
	}

	public boolean solvable() {
		return !expired() && solde.getSolde() > 0;
	}

	public boolean utilisablePour(final Utilisation utilisation) {
		return 
			numeroCompte.equals(utilisation.getNumeroCompte())
			&& id.equals(utilisation.getCartoucheId()) && utilisation.getUsage().isSubsetOf(this.getUsage())
			&& solvable()
			&& solde.deduire(utilisation.getMontant()).getSolde() >= 0;
	}
	
	@Override
	public String toString() {
		return "{id: " + id + ", compte: " + numeroCompte + ", solde: " + solde
				+ ", validite: " + validite + ", usage: " + usage + "}";
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
	protected Cartouche() {

	}

	@Override
	public int compareTo(Cartouche o) {
		final Cartouche c1 = this;
		final Cartouche c2 = o;

		final int date = c1.getValidite().compareTo(c2.getValidite());
		if (date == 0) {
			return c1.getUsage().compareTo(c2.getUsage());
		}
		return date;
	}

}
