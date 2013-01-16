package sajou.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sajou.utils.TestMillisProvider;

import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.application.services.SoldeCompte;
import com.emo.sajou.application.services.UtiliserCompte;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.SoldePourUsage;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-init.xml")
@ActiveProfiles(profiles = "production")
public class CompteIntegration {

	@Inject
	private Cartouches cartouches;
	
	@Inject
	private UtiliserCompte utiliserCompte;

	/**
	 * Créditer un porte monnaie : n jetons pour l'usage de deux services valide
	 * 6 mois. Soit un porte monnaie au solde nul. Soit un usage "envie" pour
	 * deux services "cool" et "great" Lorsqu'on crédite N jetons valide 6 mois
	 * pour l'envie Alors le porte-monnaie propose un solde pour l'envie et le
	 * porte-monnaie propose un solde pour service cool et le porte-monnaie
	 * propose un solde pour le service great et le solde du porte monnaie est
	 * de 0 à J et le solde pour l'envie est à N et le solde pour cool est à N
	 * et le solde pour great est à N et le solde pour l'envie est à N à J+12
	 * mois. et le solde pour l'envie est 0 à J+1j+12 mois
	 */
	@Test
	public void deposerPourUsageSpecifique() {
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);

		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);

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

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(0, soldeCompte.solde(compte, new Usage()));
		assertEquals(n, soldeCompte.solde(compte, envie));
		assertEquals(n, soldeCompte.solde(compte, new Usage(cool)));
		assertEquals(n, soldeCompte.solde(compte, new Usage(great)));

		timeProvider.offset(Period.months(12));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(0, soldeCompte.solde(compte, new Usage()));
		assertEquals(n, soldeCompte.solde(compte, envie));
		assertEquals(n, soldeCompte.solde(compte, new Usage(cool)));
		assertEquals(n, soldeCompte.solde(compte, new Usage(great)));

		timeProvider.offset(Period.months(12).plusDays(1));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(0, soldeCompte.solde(compte, new Usage()));
		assertEquals(0, soldeCompte.solde(compte, envie));
		assertEquals(0, soldeCompte.solde(compte, new Usage(cool)));
		assertEquals(0, soldeCompte.solde(compte, new Usage(great)));
	}

	@Test
	public void epuiserCompte() throws NonSolvableException {
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);

		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);

		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage usage = new Usage();

		final NumeroCompte compte = new NumeroCompte();
		final long n = 10 + Math.round(Math.random() * 100);

		deposer.deposer(compte, n, Period.months(12), usage);
		deposer.deposer(compte, 24, Period.months(12), new Usage(great));
		deposer.deposer(compte, 54, Period.months(6), new Usage(cool, great));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(n, soldeCompte.solde(compte, new Usage()));
		assertEquals(n + 54, soldeCompte.solde(compte, new Usage(cool)));
		assertEquals(n + 54 + 24, soldeCompte.solde(compte, new Usage(great)));

		utiliserCompte.utiliser(compte, 40, new Usage(great));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(n, soldeCompte.solde(compte, new Usage()));
		assertEquals(n + 54 + 24 - 40, soldeCompte.solde(compte, new Usage(great)));
		assertEquals(n + 54 - 40, soldeCompte.solde(compte, new Usage(cool)));

		utiliserCompte.utiliser(compte, 17, new Usage(cool));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(n - 3, soldeCompte.solde(compte, new Usage()));
		assertEquals(n + 54 + 24 - 40 - (14 + 3), soldeCompte.solde(compte, new Usage(great)));
		assertEquals(n - 3, soldeCompte.solde(compte,  new Usage(cool)));

		timeProvider.offset(Period.months(8));
		utiliserCompte.utiliser(compte, n + 54 + 24 - 40 - (14 + 3) - 3,  new Usage(great));

		assertEquals(3, soldeCompte.solde(compte, new Usage()));
		assertEquals(3, soldeCompte.solde(compte, new Usage(great)));
		assertEquals(3, soldeCompte.solde(compte, new Usage(cool)));

		utiliserCompte.utiliser(compte, 3, new Usage(great));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		assertEquals(0, soldeCompte.solde(compte, new Usage()));
		assertEquals(0, soldeCompte.solde(compte, new Usage(great)));
		assertEquals(0, soldeCompte.solde(compte, new Usage(cool)));

		try {
			utiliserCompte.utiliser(compte, 4, new Usage(great));
			fail("aurait du échouer car le solde est épuisé");
		} catch (NonSolvableException nse) {

		}
	}

}
