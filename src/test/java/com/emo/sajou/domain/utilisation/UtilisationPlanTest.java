package com.emo.sajou.domain.utilisation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;

public class UtilisationPlanTest {

	private final NumeroCompte numero = new NumeroCompte();

	@Test
	public void testSolvable() {
		final Cartouche c1 = new Cartouche(numero, Usage.ALL, Period.days(0).minus(
				Period.days(30)), 50);
		final Cartouche c2 = new Cartouche(numero, Usage.ALL, Period.months(6), 50);
		final Cartouche c3 = new Cartouche(numero, new Usage(new Service("foo")), Period
				.months(6), 50);
		final Cartouche c4 = new Cartouche(numero, new Usage(new Service("bar")), Period
				.months(3), 50);
		final Cartouche c5 = new Cartouche(numero, new Usage(new Service("foo"),
				new Service("bar")), Period.months(6), 50);
		
		final Compte compte = new Compte(Arrays.asList(c1, c2, c3, c4, c5));

		final UtilisationPlan plan = new UtilisationPlan(compte, 300, new Usage(new Service("bar")));
				
		try {
			plan.utilisations();
			fail("le plan doit échouer car le compte n'est pas solvable");
		} catch(NonSolvableException nse) {
			assertTrue(true);
		}
	}

	@Test
	public void testCalculerUnPlanSolvableSurPlusieursCartouches() {
		final Cartouche c1 = new Cartouche(numero, Usage.ALL, Period.days(0).minus(
				Period.days(30)), 50);
		final Cartouche c2 = new Cartouche(numero, Usage.ALL, Period.months(6), 50);
		final Cartouche c3 = new Cartouche(numero, new Usage(new Service("foo")), Period
				.months(6), 50);
		final Cartouche c4 = new Cartouche(numero, new Usage(new Service("bar")), Period
				.months(3), 50);
		final Cartouche c5 = new Cartouche(numero, new Usage(new Service("foo"),
				new Service("bar")), Period.months(6), 50);
		
		final Compte compte = new Compte(Arrays.asList(c1, c2, c3, c4, c5));

		final UtilisationPlan plan = new UtilisationPlan(compte, 130, new Usage(new Service("bar")));
				
		try {
			final List<Utilisation> expected = Arrays.asList( new Utilisation(numero, c4.getId(), 50, new Usage(new Service("bar"))),
					new Utilisation(numero, c5.getId(), 50, new Usage(new Service("bar"))),
					new Utilisation(numero, c2.getId(), 30, new Usage(new Service("bar"))));
			
			Assert.assertArrayEquals(expected.toArray(), plan.utilisations().toArray());
		} catch(NonSolvableException nse) {
			fail("le plan ne doit pas échouer : " + nse.getMessage());
		}
	}
}
