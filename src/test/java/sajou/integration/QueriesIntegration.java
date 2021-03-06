package sajou.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.emo.mango.cqs.DuplicateException;
import com.emo.mango.cqs.QueryExecutor;
import com.emo.mango.spring.cqs.support.MangoCQS;
import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.queries.CartoucheJpql;
import com.emo.sajou.queries.CartoucheSql;
import com.emo.sajou.queries.OperationSummary;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-init.xml")
@ActiveProfiles(profiles = {"mangods", "mysql"})
public class QueriesIntegration {

	@Inject
	private MangoCQS cqs;

	@Inject
	private Cartouches cartouches;
	
	@Test
	public void testerCartoucheJpql() throws DuplicateException {
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);

		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage envie = new Usage(cool, great);

		final NumeroCompte compte = new NumeroCompte();
		final long n = 10 + Math.round(Math.random() * 100);

		deposer.deposer(compte, n, Period.months(12), envie);
		deposer.deposer(compte, 25, Period.months(6), new Usage(new Service(
				"something")));
		deposer.deposer(compte, 35, Period.months(12), new Usage(new Service(
				"something")));
		for (int i = 0; i < 30; ++i) {
			deposer.deposer(compte, 35 + i, Period.months(12 + i), new Usage(
					new Service("cool")));
		}

		final QueryExecutor e = cqs.system().getQueryExecutor("findCartoucheByCompte");

		List<CartoucheJpql> cartouches = e.query(CartoucheJpql.class, compte.getCompte());

		for (final CartoucheJpql c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.countItems(compte.getCompte());
		final int pages = e.countPages(10, compte.getCompte());
		final List<CartoucheJpql> pagedCartouche = e.pagedQuery(CartoucheJpql.class, 2, 10, compte.getCompte());
	
		assertEquals(33, items);
		assertEquals(4, pages);
		
		int i = 0;
		for (final CartoucheJpql c : pagedCartouche) {
			assertEquals(compte.getCompte(), c.compte);
			assertEquals(35 + 10 - 3 + i, c.solde);
			++i;
		}

	}

	
	@Test
	public void testerCartoucheSql() throws DuplicateException {
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);

		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage envie = new Usage(cool, great);

		final NumeroCompte compte = new NumeroCompte();
		final long n = 10 + Math.round(Math.random() * 100);

		deposer.deposer(compte, n, Period.months(12), envie);
		deposer.deposer(compte, 25, Period.months(6), new Usage(new Service(
				"something")));
		deposer.deposer(compte, 35, Period.months(12), new Usage(new Service(
				"something")));
		for (int i = 0; i < 30; ++i) {
			deposer.deposer(compte, 35 + i, Period.months(12 + i), new Usage(
					new Service("cool")));
		}

		final QueryExecutor e = cqs.system().getQueryExecutor(
				"findCartoucheByCompteAndSoldeAbove");

		List<CartoucheSql> cartouches = e.query(CartoucheSql.class, compte.getCompte(), 0);

		for (final CartoucheSql c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.countItems(compte.getCompte(), 0);
		final int pages = e.countPages(10, compte.getCompte(), 0);
		
		@SuppressWarnings("unchecked")
		final List<CartoucheSql> pagedCartouche = (List<CartoucheSql>) e.pagedQuery(2, 10, compte.getCompte(), 0);
	
		assertEquals(33, items);
		assertEquals(4, pages);
		
		int i = 0;
		for (final CartoucheSql c : pagedCartouche) {
			assertEquals(compte.getCompte(), c.compte);
			assertEquals(new Long(35 + 10 - 3 + i), c.solde);
			++i;
		}

	}
	
	@Test
	public void testAllOperations() {
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);

		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage envie = new Usage(cool, great);

		final NumeroCompte compte = new NumeroCompte();
		final long n = 10 + Math.round(Math.random() * 100);

		deposer.deposer(compte, n, Period.months(12), envie);
		deposer.deposer(compte, 25, Period.months(6), new Usage(new Service(
				"something")));
		deposer.deposer(compte, 35, Period.months(12), new Usage(new Service(
				"something")));
		for (int i = 0; i < 30; ++i) {
			deposer.deposer(compte, 35 + i, Period.months(12 + i), new Usage(
					new Service("cool")));
		}

		final QueryExecutor e = cqs.system().getQueryExecutor(
				"allOperationsForCompte");

		List<OperationSummary> ops = e.query(OperationSummary.class, compte.getCompte());

		for (final OperationSummary op : ops) {
			System.out.println(op);
		}		
	}
	
}
