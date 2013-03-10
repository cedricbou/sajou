package com.emo.sajou.commands;

import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.MinSize;
import net.sf.oval.constraint.NotNull;

import com.emo.mango.log.LogParam;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeposerPourUnUsage {

	@NotNull
	@MinLength(3)
	@LogParam
	public final String numeroCompte;

	@Min(1)
	@LogParam
	public final long montant;

	@Min(1)
	@LogParam
	public final int validiteEnMois;
	
	@NotNull
	@MinSize(1)
	@LogParam
	public final String[] services;

	@JsonCreator
	public DeposerPourUnUsage(final @JsonProperty("numeroCompte") String compte,
			final @JsonProperty("montant") long montant,
			final @JsonProperty("validiteEnMois") int validiteEnMois,
			final @JsonProperty("services") String[] services) {
		this.numeroCompte = compte;
		this.montant = montant;
		this.validiteEnMois = validiteEnMois;
		this.services = services;
	}
}
