package com.emo.sajou.reports;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emo.mango.annotations.Final;
import com.emo.mango.spring.cqs.support.MangoCQS;
import com.emo.mango.spring.web.support.QueryToCsv;

@Component
public class Reports {

	private final static Logger logger = LoggerFactory.getLogger(Reports.class);
	
	@Inject
	private @Final
	MangoCQS cqs;

	@Scheduled(cron="* */5 * * * *")
	public void reportCCC() {
		report("cedric.boufflers@gmail.com", "findCartoucheByCompte", "ccc");
	}

	private void report(final String email, final String name, Object... args) {
		try {
			logger.info("running report {} for {}", name, email);
			
			final File report = new QueryToCsv(cqs.system().getQueryExecutor(
					name), args).generate();
			new Reporter("cedric.boufflers@gmail.com", "cedric.boufflers@gmail.com", name, report).send();
		} catch (IOException ioe) {
			throw new RuntimeException("failed to generate report", ioe);
		}
	}
}
