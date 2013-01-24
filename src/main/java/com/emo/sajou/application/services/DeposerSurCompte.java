package com.emo.sajou.application.services;

import javax.inject.Inject;

import org.joda.time.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

@Service
public class DeposerSurCompte {

	private /* should be final */ Cartouches cartouches;
	
	@Inject
	public DeposerSurCompte(final Cartouches cartouches) {
		this.cartouches = cartouches;
	}
	
	@Transactional
	public void deposer(final NumeroCompte numero, final long montant, final Period validite, final Usage usage) {
		final Cartouche cartouche = new Cartouche(numero, usage, validite, montant);
		cartouches.save(cartouche);
	}
	
	@Deprecated
	protected DeposerSurCompte() {
		
	}
}
