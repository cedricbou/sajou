package com.emo.sajou.application.services;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emo.mango.annotations.Final;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.operation.Operation;
import com.emo.sajou.domain.operation.Operations;
import com.emo.sajou.domain.utilisation.Utilisation;
import com.emo.sajou.domain.utilisation.UtilisationPlan;
import com.emo.sajou.domain.utilisation.Utilisations;

@Service
public class UtiliserCompte {

	private @Final Cartouches cartouches;
	private @Final Utilisations utilisations;
	private @Final Operations operations;
	
	@Inject
	public UtiliserCompte(final Cartouches cartouches, final Utilisations utilisations, final Operations operations) {
		this.cartouches = cartouches;
		this.utilisations = utilisations;
		this.operations = operations;
	}
	
	@Transactional
	public void utiliser(final Operation op) throws NonSolvableException {
		final Compte compte = new Compte(this.cartouches.findByNumeroCompte(op.getNumeroCompte()));
				
		final UtilisationPlan plan = new UtilisationPlan(compte, op);

		final List<Utilisation> utilisations = plan.utilisations();
		compte.appliquer(utilisations);
				
		for(final Utilisation utilisation : utilisations) {
			this.utilisations.save(utilisation);
		}
		
		this.operations.save(op);
		
	}
	
	@Deprecated
	protected UtiliserCompte() {
		
	}

}
