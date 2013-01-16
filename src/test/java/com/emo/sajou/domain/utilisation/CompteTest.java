package com.emo.sajou.domain.utilisation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;

import com.emo.sajou.domain.cartouche.Cartouche;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.Compte;
import com.emo.sajou.domain.compte.NonSolvableException;
import com.emo.sajou.domain.compte.NumeroCompte;

public class CompteTest {

	private final NumeroCompte numero = new NumeroCompte();

	private final Cartouche c1 = new Cartouche(numero, Usage.ALL, Period.days(0).minus(
			Period.days(30)), 50);
	private final Cartouche c2 = new Cartouche(numero, Usage.ALL, Period.months(6), 50);
	private final Cartouche c3 = new Cartouche(numero, new Usage(new Service("foo")), Period
			.months(6), 50);
	private final Cartouche c4 = new Cartouche(numero, new Usage(new Service("bar")), Period
			.months(3), 50);
	private final Cartouche c5 = new Cartouche(numero, new Usage(new Service("foo"),
			new Service("bar")), Period.months(6), 50);
	
	private final Compte compte = new Compte(Arrays.asList(c1, c2, c3, c4, c5));
	
	@Test
	public void testAppliquer() {
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
			compte.appliquer(plan.utilisations());
			
			assertEquals(0, c4.getSolde().getSolde());
			assertEquals(0, c5.getSolde().getSolde());
			assertEquals(20, c2.getSolde().getSolde());
			
			assertEquals(50, c1.getSolde().getSolde());
			assertEquals(50, c3.getSolde().getSolde());
		} catch(NonSolvableException nse) {
			fail("le plan ne doit pas échouer : " + nse.getMessage());
		}
	}

	@Test
	public void testGetNumero() {
		assertEquals(numero, compte.getNumero());
	}

	@Test
	public void testGetSortedCartouches() {
		Assert.assertArrayEquals(Arrays.asList(c1, c4, c3, c5, c2).toArray(), compte.getSortedCartouches().toArray());
	}

	@Test
	public void testSolvable() {
		assertTrue(compte.solvable());
		
		final Compte c2 = new Compte(Arrays.asList(new Cartouche(numero, new Usage(), Period.months(1), 0)));
		assertFalse(c2.solvable());

		final Compte c3 = new Compte(Arrays.asList(new Cartouche(numero, new Usage(), Period.days(0).minusDays(1), 10)));
		assertFalse(c3.solvable());
	}

	@Test
	public void testSolvableFor() {
		assertTrue(compte.solvableFor(new Usage(new Service("foo"))));
		
		final Compte c2 = new Compte(Arrays.asList(new Cartouche(numero, new Usage(new Service("bar")), Period.months(1), 0)));
		assertFalse(c2.solvableFor(new Usage(new Service("bar"))));

		final Compte c3 = new Compte(Arrays.asList(new Cartouche(numero, Usage.ALL, Period.days(0).minusDays(1), 10)));
		assertFalse(c3.solvableFor(new Usage(new Service("bar"))));
	}

}
