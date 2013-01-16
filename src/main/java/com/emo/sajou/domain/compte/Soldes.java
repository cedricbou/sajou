package com.emo.sajou.domain.compte;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.commons.Solde;
import com.emo.sajou.domain.commons.SoldePourUsage;
import com.emo.sajou.domain.commons.Usage;

public class Soldes {

	private final Compte compte;
	
	public Soldes(final Compte compte) {
		this.compte = compte;
	}
	
	public long solde(final Usage usage) {
		long solde = 0;
		
		for(final Cartouche cartouche : compte.getSortedCartouches()) {
			if(cartouche.accepte(usage) && cartouche.solvable()) {
				solde += cartouche.getSolde().getSolde();
			}
		}
		
		return solde;
	}
	
	public List<SoldePourUsage> soldeParUsage() {
		final List<Cartouche> cartouches = compte.getSortedCartouches();
		final Map<Usage, Solde> usages = new HashMap<Usage, Solde>();
		
		for(final Cartouche cartouche : cartouches) {
			if(cartouche.solvable()) {
				if(usages.containsKey(cartouche.getUsage())) {
					usages.put(cartouche.getUsage(), cartouche.getSolde().ajouter(usages.get(cartouche.getUsage()).getSolde()));
				}
				else {
					usages.put(cartouche.getUsage(), cartouche.getSolde());
				}
			}
		}
		
		final List<SoldePourUsage> soldes = new LinkedList<SoldePourUsage>();
		
		final List<Usage> sortedUsages = new LinkedList<Usage>(usages.keySet());
		Collections.sort(sortedUsages);
		
		for(final Usage usage : sortedUsages) {
			soldes.add(new SoldePourUsage(usage, usages.get(usage)));
		}
		
		return soldes;
	}
}
