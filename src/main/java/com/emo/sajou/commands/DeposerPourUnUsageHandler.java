package com.emo.sajou.commands;

import javax.inject.Inject;

import org.joda.time.Period;
import org.springframework.stereotype.Component;

import com.emo.mango.cqs.Handler;
import com.emo.mango.spring.cqs.annotations.CommandHandler;
import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

@Component
@CommandHandler(command = DeposerPourUnUsage.class)
public class DeposerPourUnUsageHandler implements Handler<DeposerPourUnUsage>{

	private @Inject DeposerSurCompte deposer;
	
	@Override
	public void handle(DeposerPourUnUsage cmd) {
		Usage usage = new Usage(new Service(cmd.services[0]));
		for(int i = 1; i < cmd.services.length; ++i) {
			usage = usage.withService(new Service(cmd.services[i]));
		}
		
		deposer.deposer(new NumeroCompte(cmd.numeroCompte),
			cmd.montant,
			Period.months(cmd.validiteEnMois),
			usage);
	}
}
