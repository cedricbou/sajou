package com.emo.sajou.application.services;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.utilisation.Utilisation;
import com.emo.sajou.domain.utilisation.UtilisationPlan;
import com.emo.sajou.domain.utilisation.Utilisations;

@Service
public class UtiliserCompte {

	private /* should be final */ Cartouches cartouches;
	private /* should be final */ Utilisations utilisations;
	
	@Inject
	public UtiliserCompte(final Cartouches cartouches, final Utilisations utilisations) {
		this.cartouches = cartouches;
		this.utilisations = utilisations;
	}
	
	@Transactional
	public void utiliser(final NumeroCompte numero, final long montant, final Usage usage) throws NonSolvableException {
		final Compte compte = new Compte(this.cartouches.findByNumeroCompte(numero));
		final UtilisationPlan plan = new UtilisationPlan(compte, montant, usage);

		final List<Utilisation> utilisations = plan.utilisations();
		compte.appliquer(utilisations);
		
		for(final Utilisation utilisation : utilisations) {
			this.utilisations.save(utilisation);
		}
	}
	
	@Deprecated
	protected UtiliserCompte() {
		
	}

}
