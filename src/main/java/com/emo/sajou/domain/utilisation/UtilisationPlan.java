package com.emo.sajou.domain.utilisation;

import java.util.LinkedList;
import java.util.List;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.operation.Operation;
import com.emo.sajou.domain.operation.OperationId;

public class UtilisationPlan {

	private final Compte compte;
	private final Usage usage;
	private final long montant;
	private final OperationId operationId;

	public UtilisationPlan(final Compte compte, final Operation operation) {
		this.compte = compte;
		this.montant = operation.getMontant();
		this.usage = operation.getUsage();
		this.operationId = operation.getId();
	}

	public List<Utilisation> utilisations() throws NonSolvableException {
		final List<Utilisation> utilisations = new LinkedList<Utilisation>();
		
		long reste = montant;
		
		for(final Cartouche c : compte.getSortedCartouches()) {
			final Utilisation candidate = new Utilisation(compte.getNumero(), c.getId(), Math.max(0, Math.min(c.getSolde().getSolde(), reste)), usage, operationId);
			if(c.utilisablePour(candidate)) {
				utilisations.add(candidate);
				reste -= candidate.getMontant();
			}
		}
		
		if(reste > 0) {
			throw new NonSolvableException("solde insuffisant pour le compte " + compte.getNumero() + " en utilisant le montant " + montant + " pour " + usage);
		}
		
		return utilisations;
	}
}
