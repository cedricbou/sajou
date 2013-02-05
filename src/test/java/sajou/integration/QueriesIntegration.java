package sajou.integration;

import static com.emo.mango.cqs.PropertyBasedSearchValue.property;
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
import com.emo.mango.cqs.ObjectBasedSearchValue;
import com.emo.mango.cqs.PropertyBasedSearchValue;
import com.emo.mango.cqs.QueryExecutor;
import com.emo.mango.spring.cqs.support.MangoCQS;
import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.queries.Cartouche2SqlExecutor.SearchCartouche;
import com.emo.sajou.queries.CartoucheJpql;
import com.emo.sajou.queries.CartoucheSql;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-init.xml")
@ActiveProfiles(profiles = {"mangods", "mysql"})
public class QueriesIntegration {

	@Inject
	private MangoCQS cqs;

	@Inject
	private Cartouches cartouches;

	private PropertyBasedSearchValue fromCompte(final NumeroCompte compte) {
		return new PropertyBasedSearchValue(property("compte", compte.getCompte()));
	}
	
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

		final QueryExecutor<?> e = cqs.system().queryExecutor(
				CartoucheJpql.class.getSimpleName());

		@SuppressWarnings("unchecked")
		List<CartoucheJpql> cartouches = (List<CartoucheJpql>) e.query(fromCompte(compte));

		for (final CartoucheJpql c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.countItems(fromCompte(compte));
		final int pages = e.countPages(10, fromCompte(compte));
		final List<CartoucheJpql> pagedCartouche = (List<CartoucheJpql>) e.pagedQuery(2, 10, fromCompte(compte));
	
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

		final QueryExecutor<?> e = cqs.system().queryExecutor(
				CartoucheSql.class.getSimpleName());

		@SuppressWarnings("unchecked")
		List<CartoucheSql> cartouches = (List<CartoucheSql>) e.query(fromCompte(compte));

		for (final CartoucheSql c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.simpleCountItems(compte.getCompte());
		final int pages = e.simpleCountPages(10, compte.getCompte());
		final List<CartoucheSql> pagedCartouche = (List<CartoucheSql>) e.simplePagedQuery(2, 10, compte.getCompte());
	
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
	public void testerCartouche2Sql() throws DuplicateException {
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

		final QueryExecutor<?> e = cqs.system().queryExecutor("Cartouche2Sql");

		@SuppressWarnings("unchecked")
		List<CartoucheSql> cartouches = (List<CartoucheSql>) e.objectQuery(new SearchCartouche(compte.getCompte()));

		for (final CartoucheSql c : cartouches) {
			assertEquals(compte.getCompte(), c.compte);
		}

		final long items = e.objectCountItems(new SearchCartouche(compte.getCompte()));
		final int pages = e.objectCountPages(10, new SearchCartouche(compte.getCompte()));
		final List<CartoucheSql> pagedCartouche = (List<CartoucheSql>) e.objectPagedQuery(2, 10, new SearchCartouche(compte.getCompte()));
	
		assertEquals(33, items);
		assertEquals(4, pages);
		
		int i = 0;
		for (final CartoucheSql c : pagedCartouche) {
			assertEquals(compte.getCompte(), c.compte);
			assertEquals(new Long(35 + 10 - 3 + i), c.solde);
			++i;
		}

	}

	
}
