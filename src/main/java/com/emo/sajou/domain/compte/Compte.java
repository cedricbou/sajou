package com.emo.sajou.domain.compte;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.cartouche.CartoucheId;
import com.emo.sajou.domain.commons.Solde;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.utilisation.Utilisation;

public class Compte {
	private final List<Cartouche> cartouches;
	
	private final NumeroCompte numero;
	
	public Compte(final List<Cartouche> cartouches) {
		this.cartouches = new LinkedList<Cartouche>(cartouches);

		if(this.cartouches.size() == 0) {
			throw new IllegalArgumentException("Dans un compte, il faut au moins un cartouche");
		}
		
		numero = cartouches.get(0).getNumeroCompte();
			
		for(final Cartouche cartouche : cartouches) {
			if(!cartouche.getNumeroCompte().equals(numero)) {
				throw new IllegalArgumentException("Dans un compte, tous les cartouches doivent référencer le même numéro de compte, ce compte :" + numero);
			}
		}

		Collections.sort(this.cartouches);
	}
	
	public synchronized void appliquer(final List<Utilisation> utilisations) throws NonSolvableException {
		boolean failed = false;
		
		final Map<CartoucheId, Solde> rollbackData = new HashMap<CartoucheId, Solde>();
		
		for (final Utilisation utilisation : utilisations) {
			final Cartouche cartouche = findCartouche(utilisation.getCartoucheId());
			
			if (cartouche.utilisablePour(utilisation)) {
				rollbackData.put(cartouche.getId(), cartouche.getSolde());
				cartouche.deduire(utilisation.getMontant());
			}
			else {
				failed = true;
				break;
			}
		}
		
		if(failed) {
			for (final CartoucheId id : rollbackData.keySet()) {
				for (final Cartouche cartouche : cartouches) {
					if (cartouche.getId().equals(id)) {
						cartouche.ajouter(rollbackData.get(id).getSolde());
						break;
					}
				}
			}
			throw new NonSolvableException("impossible d'appliquer le plan, un des cartouches n'est pas solvable.");
		}
	}

	private Cartouche findCartouche(final CartoucheId cartoucheId) {
		for (final Cartouche cartouche : cartouches) {
			if(cartoucheId.equals(cartouche.getId())) {
				return cartouche;
			}
		}
		
		throw new IllegalStateException("recherche d'un cartouche non existant pour ce compte " + cartoucheId);
	}
	
	public NumeroCompte getNumero() {
		return numero;
	}
	
	public List<Cartouche> getSortedCartouches() {
		return Collections.unmodifiableList(cartouches);
	}
	
	public boolean solvable() {
		boolean solvable = false;
		for(final Cartouche c : cartouches) {
			solvable |= c.solvable();
		}
		return solvable;
	}

	public boolean solvableFor(final Usage usage) {
		boolean solvable = false;
		for(final Cartouche c : cartouches) {
			if(c.accepte(usage)) {
				solvable |= c.solvable();
			}
		}
		return solvable;
	}
}
