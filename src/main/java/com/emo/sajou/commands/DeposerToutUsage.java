package com.emo.sajou.commands;

import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeposerToutUsage {

	@NotNull
	@MinLength(3)
	public final String compte;

	@Min(1)
	public final long montant;

	@Min(1)
	public final int validiteEnMois;

	@JsonCreator
	public DeposerToutUsage(final @JsonProperty("compte") String compte,
			final @JsonProperty("montant") long montant,
			final @JsonProperty("validiteEnMois") int validiteEnMois) {
		this.compte = compte;
		this.montant = montant;
		this.validiteEnMois = validiteEnMois;
	}
}
