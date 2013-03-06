package com.emo.sajou.commands;

import javax.inject.Inject;

import org.joda.time.Period;

import com.emo.mango.cqs.Handler;
import com.emo.mango.spring.cqs.annotations.CommandHandler;
import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

@CommandHandler(command = DeposerToutUsage.class)
public class DeposerToutUsageHandler implements Handler<DeposerToutUsage>{

	private @Inject DeposerSurCompte deposer;
	
	@Override
	public void handle(DeposerToutUsage cmd) {
		deposer.deposer(new NumeroCompte(cmd.compte),
			cmd.montant,
			Period.months(cmd.validiteEnMois),
			Usage.ALL);
	}
}
