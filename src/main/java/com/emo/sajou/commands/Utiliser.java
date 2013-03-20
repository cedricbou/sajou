package com.emo.sajou.commands;

import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Utiliser {

	@NotNull
	@MinLength(3)
	public final String compte;

	@Min(1)
	public final long montant;

	@JsonCreator
	public Utiliser(final @JsonProperty("compte") String compte,
			final @JsonProperty("montant") long montant) {
		this.compte = compte;
		this.montant = montant;
	}

}
