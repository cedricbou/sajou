package com.emo.sajou.application.services;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.SoldePourUsage;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.compte.Soldes;

@Service
public class SoldeCompte {

	private /* should be final */ Cartouches cartouches;
	
	@Inject
	public SoldeCompte(final Cartouches cartouches) {
		this.cartouches = cartouches;
	}
	
	@Transactional
	public long solde(final NumeroCompte numero, final Usage usage) {
		final List<Cartouche> cartouches = this.cartouches.findByNumeroCompte(numero);
		
		final Soldes soldes = new Soldes(new Compte(cartouches));
		
		return soldes.solde(usage);
	}
	
	@Transactional
	public List<SoldePourUsage> soldeParUsage(final NumeroCompte numero) {
		final List<Cartouche> cartouches = this.cartouches.findByNumeroCompte(numero);

		final Soldes soldes = new Soldes(new Compte(cartouches));
		
		return soldes.soldeParUsage();
	}
	
	@Deprecated
	protected SoldeCompte() {
		
	}
}
