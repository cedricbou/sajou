package sajou.integration;

import static org.junit.Assert.*;

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
import com.emo.sajou.queries.CartoucheQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-init.xml")
@ActiveProfiles(profiles = {"mangods", "mysql"})
public class QueriesIntegration {

	@Inject
	private MangoCQS cqs;

	@Inject
	private Cartouches cartouches;

	@Test
	public void testerCartoucheQuery() throws DuplicateException {
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

		final QueryExecutor<?> e = cqs.system().queryExecutor(
				CartoucheQuery.class.getSimpleName());

		@SuppressWarnings("unchecked")
		List<CartoucheQuery> cartouches = (List<CartoucheQuery>) e.query();

		for (final CartoucheQuery c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.countItems();
		final int pages = e.countPages(10);
		final List<CartoucheQuery> pagedCartouche = (List<CartoucheQuery>) e.pagedQuery(2, 10);
	
		assertEquals(33, items);
		assertEquals(4, pages);
		
		int i = 0;
		for (final CartoucheQuery c : pagedCartouche) {
			assertEquals(compte.getCompte(), c.compte);
			assertEquals(35 + 10 - 3 + i, c.solde);
			++i;
		}

	}

}
