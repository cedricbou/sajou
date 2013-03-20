package com.emo.sajou.commands;

import javax.inject.Inject;

import com.emo.mango.cqs.Handler;
import com.emo.mango.spring.cqs.annotations.CommandHandler;
import com.emo.sajou.application.services.UtiliserCompte;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.operation.Operation;

@CommandHandler(command = Utiliser.class)
public class UtiliserHandler implements Handler<Utiliser> {
	
	public final static String NON_SOLVABLE_EXCEPTION = "NonSolvable";

	@Inject
	private UtiliserCompte utiliserCompte;
	
	@Override
	public void handle(Utiliser cmd) {
		try {
			utiliserCompte.utiliser(new Operation(new NumeroCompte(cmd.compte), Usage.ALL, cmd.montant));
		}
		catch(NonSolvableException nse) {
			throw new IllegalStateException(NON_SOLVABLE_EXCEPTION, nse);
		}
	
	}
}
