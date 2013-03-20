package sajou.domain.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.junit.Test;
import org.mockito.Mockito;

import sajou.utils.TestMillisProvider;

import com.emo.sajou.application.services.DeposerSurCompte;
import com.emo.sajou.application.services.SoldeCompte;
import com.emo.sajou.application.services.UtiliserCompte;
import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.cartouche.Cartouches;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.SoldePourUsage;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.operation.Operation;
import com.emo.sajou.domain.operation.OperationId;
import com.emo.sajou.domain.operation.Operations;
import com.emo.sajou.domain.utilisation.Utilisation;
import com.emo.sajou.domain.utilisation.Utilisations;

public class CompteBddTest {

	/**
	 * Créditer un porte monnaie : n jetons pour tout usage valide 12 mois. Soit
	 * un porte monnaie au solde nul. Lorsqu'on crédite N jetons valide 12 mois
	 * Alors le solde du porte monnaie est de N à J et le solde est 0 à J+1+12
	 * mois. et le solde est N à J+12 mois.
	 */
	@Test
	public void deposerToutUsage() {
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);

		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);

		final long n = 10 + Math.round(Math.random() * 100);

		final NumeroCompte compte = new NumeroCompte();

		final Cartouche expected = new Cartouche(compte, new Usage(),
				Period.months(12), n);

		deposer.deposer(compte, n, Period.months(12), new Usage());
		Mockito.verify(cartouches).save(expected);

		Mockito.when(cartouches.findByNumeroCompte(compte)).thenReturn(
				Arrays.asList(expected));
		assertEquals(n, soldeCompte.solde(compte, new Usage()));

		timeProvider.offset(Period.months(12));

		assertEquals(n, soldeCompte.solde(compte, new Usage()));

		timeProvider.offset(Period.months(12).plusDays(1));

		assertEquals(0, soldeCompte.solde(compte, new Usage()));
	}

	/**
	 * Créditer un porte monnaie : n jetons pour tout usage valide 12 mois. Soit
	 * un porte monnaie au solde nul. Lorsqu'on crédite N jetons valide 12 mois
	 * Alors le solde du porte monnaie est de N à J et le solde est 0 à J+1+12
	 * mois. et le solde est N à J+12 mois.
	 */
	@Test
	public void deposerToutUsageSurDifferentsComptes() {
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);

		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);

		final int nbc = 3; // nombre de comptes.

		final long[] n = new long[nbc];
		final NumeroCompte[] compte = new NumeroCompte[nbc];
		final Cartouche[] expected = new Cartouche[nbc];

		for (int i = 0; i < nbc; ++i) {
			n[i] = 10 + Math.round(Math.random() * 100);
			compte[i] = new NumeroCompte();
			expected[i] = new Cartouche(compte[i], new Usage(),
					Period.months(12), n[i]);
		}

		for (int i = 0; i < nbc; ++i) {
			deposer.deposer(compte[i], n[i], Period.months(12), new Usage());
			Mockito.verify(cartouches).save(expected[i]);
			Mockito.when(cartouches.findByNumeroCompte(compte[i])).thenReturn(
					Arrays.asList(expected[i]));
		}

		for (int i = 0; i < nbc; ++i) {
			assertEquals(n[i], soldeCompte.solde(compte[i], new Usage()));
		}

		timeProvider.offset(Period.months(12));

		for (int i = 0; i < nbc; ++i) {
			assertEquals(n[i], soldeCompte.solde(compte[i], new Usage()));
		}

		timeProvider.offset(Period.months(12).plusDays(1));

		for (int i = 0; i < nbc; ++i) {
			assertEquals(0, soldeCompte.solde(compte[i], new Usage()));
		}
	}

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
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);

		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);

		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage envie = new Usage(cool, great);

		final NumeroCompte compte = new NumeroCompte();
		final long n = 10 + Math.round(Math.random() * 100);

		final Cartouche expected = new Cartouche(compte, envie,
				Period.months(12), n);
		final Cartouche expectedBis1 = new Cartouche(compte, new Usage(
				new Service("something")), Period.months(6), 24);
		final Cartouche expectedBis2 = new Cartouche(compte, new Usage(
				new Service("something")), Period.months(12), 34);

		deposer.deposer(compte, n, Period.months(12), envie);
		deposer.deposer(compte, 24, Period.months(6), new Usage(new Service(
				"something")));
		deposer.deposer(compte, 34, Period.months(12), new Usage(new Service(
				"something")));

		Mockito.verify(cartouches).save(expected);
		Mockito.verify(cartouches).save(expectedBis1);
		Mockito.verify(cartouches).save(expectedBis2);

		Mockito.when(cartouches.findByNumeroCompte(compte)).thenReturn(
				Arrays.asList(expected, expectedBis1, expectedBis2));

		System.out.println("--> soldes : ");
		for (final SoldePourUsage spu : soldeCompte.soldeParUsage(compte)) {
			System.out.println(spu);
		}

		// assertTrue(ArrayUtils.contains(pm.usagesDisponibles(), envie));
		// assertTrue(ArrayUtils.contains(pm.servicesDisponibles(), cool));
		// assertTrue(ArrayUtils.contains(pm.servicesDisponibles(), great));

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
	public void utiliserDesJetonsToutUsage() {
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final Utilisations utilisations = Mockito.mock(Utilisations.class);
		final Operations operations = Mockito.mock(Operations.class);
		
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);
		final UtiliserCompte utiliserCompte = new UtiliserCompte(cartouches, utilisations, operations);
		
		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);
		
		final NumeroCompte numero = new NumeroCompte();
		final long montant = 37;
		final Usage usage = new Usage();
		
		final Cartouche expected = new Cartouche(numero, usage,
				Period.months(3), 37);
		
		deposer.deposer(numero, montant, Period.months(3), usage);

		Mockito.verify(cartouches).save(expected);

		Mockito.when(cartouches.findByNumeroCompte(numero)).thenReturn(Arrays.asList(expected));
		assertEquals(montant, soldeCompte.solde(numero, usage));
		
		try {
			final Operation op1 = new Operation(numero, usage, 28);
			utiliserCompte.utiliser(op1);
			Mockito.verify(utilisations).save(new Utilisation(numero, expected.getId(), 28, usage, op1.getId()));
			assertEquals(37 - 28, soldeCompte.solde(numero, usage));

			final Operation op2 = new Operation(numero, usage, 37 - 28);
			utiliserCompte.utiliser(op2);
			Mockito.verify(utilisations).save(new Utilisation(numero, expected.getId(), 37 - 28, usage, op2.getId()));
			assertEquals(0, soldeCompte.solde(numero, usage));
		}
		catch(NonSolvableException nse) {
			fail("le compte devrait être solvable ! " + nse.getMessage());
		}
	}
	
	@Test
	public void utiliserDesJetonsPourUsageSpecifique() {
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final Utilisations utilisations = Mockito.mock(Utilisations.class);
		final Operations operations = Mockito.mock(Operations.class);
				
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);
		final UtiliserCompte utiliserCompte = new UtiliserCompte(cartouches, utilisations, operations);
		
		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);
		
		final NumeroCompte numero = new NumeroCompte();
		final long montant = 37;
		final Service cool = new Service("cool");
		final Service great = new Service("great");
		final Usage usage = new Usage(great, cool);
		
		final Cartouche expected = new Cartouche(numero, usage,
				Period.months(3), 37);
		
		deposer.deposer(numero, montant, Period.months(3), usage);

		Mockito.verify(cartouches).save(expected);

		Mockito.when(cartouches.findByNumeroCompte(numero)).thenReturn(Arrays.asList(expected));
		assertEquals(montant, soldeCompte.solde(numero, usage));
		
		try {
			final Operation op1 = new Operation(numero, new Usage(great), 14);
			utiliserCompte.utiliser(op1);
			Mockito.verify(utilisations).save(new Utilisation(numero, expected.getId(), 14, new Usage(great), op1.getId()));
			assertEquals(37 - 14, soldeCompte.solde(numero, usage));

			final Operation op2 = new Operation(numero, new Usage(cool), 14);
			utiliserCompte.utiliser(op2);
			Mockito.verify(utilisations).save(new Utilisation(numero, expected.getId(), 14, new Usage(cool), op2.getId()));
			assertEquals(37 - 14 - 14, soldeCompte.solde(numero, usage));

			final Operation op3 = new Operation(numero, usage, 37 - 14 - 14);
			utiliserCompte.utiliser(op3);
			Mockito.verify(utilisations).save(new Utilisation(numero, expected.getId(), 37 - 14 - 14, usage, op3.getId()));
			assertEquals(0, soldeCompte.solde(numero, usage));
		}
		catch(NonSolvableException nse) {
			fail("le compte devrait être solvable ! " + nse.getMessage());
		}
	}
	
	@Test
	public void utiliserDesJetonsSansSoldes() {
		final Cartouches cartouches = Mockito.mock(Cartouches.class);
		final Utilisations utilisations = Mockito.mock(Utilisations.class);
		final Operations operations = Mockito.mock(Operations.class);
				
		final DeposerSurCompte deposer = new DeposerSurCompte(cartouches);
		final SoldeCompte soldeCompte = new SoldeCompte(cartouches);
		final UtiliserCompte utiliserCompte = new UtiliserCompte(cartouches, utilisations, operations);
		
		final TestMillisProvider timeProvider = new TestMillisProvider();
		DateTimeUtils.setCurrentMillisProvider(timeProvider);
		
		final NumeroCompte numero = new NumeroCompte();
		final long montant = 37;
		final Usage usage = new Usage();
		
		final Cartouche expected = new Cartouche(numero, usage,
				Period.months(3), 37);
		
		deposer.deposer(numero, montant, Period.months(3), usage);

		Mockito.verify(cartouches).save(expected);

		Mockito.when(cartouches.findByNumeroCompte(numero)).thenReturn(Arrays.asList(expected));
		assertEquals(montant, soldeCompte.solde(numero, usage));
		
		try {
			utiliserCompte.utiliser(new Operation(numero, usage, 44));
			fail("Utiliser le compte aurait du échouer, il n'est pas solvable, on ne peut pas l'utiliser.");
		}
		catch(NonSolvableException nse) {
			assertTrue("must fail as provision in not enough", true);
		}
		
	}
}
