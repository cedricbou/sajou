package com.emo.sajou.domain.cartouche;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;

import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Solde;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;
import com.emo.sajou.domain.utilisation.Utilisation;

public class CartoucheTest {

	final LocalDate today = LocalDate.now();
	final NumeroCompte compte = new NumeroCompte();
	final Cartouche c = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(40), 30);
	
	@Test
	public void testGetUsage() {
		assertEquals(new Usage(new Service("foo"), new Service("bar")), c.getUsage());
	}

	@Test
	public void testGetValidite() {
		assertEquals(today.plusDays(40), c.getValidite());
	}

	@Test
	public void testGetNumeroCompte() {
		assertEquals(compte, c.getNumeroCompte());
	}

	@Test
	public void testGetSolde() {
		assertEquals(new Solde(30), c.getSolde());
	}

	@Test
	public void testDeduire() {
		final Cartouche c1 = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(40), 30);
		c1.deduire(20);
		assertEquals(new Solde(10), c1.getSolde());
		c1.deduire(15);
		assertEquals(new Solde(-5), c1.getSolde());
	}

	@Test
	public void testAjouter() {
		final Cartouche c1 = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(40), 30);
		c1.ajouter(10);
		assertEquals(new Solde(40), c1.getSolde());
	}

	@Test
	public void testAccepteUnUsageSiCestUnSubSetDuUsageDuCartouche() {
		assertTrue(c.accepte(new Usage(new Service("foo"))));
	}

	@Test
	public void testRefuseUnUsageSiCeNestPasUnSubSetDuUsageDuCartouche() {
		assertFalse(c.accepte(new Usage(new Service("foo"), new Service("bar"), new Service("toto"))));
	}

	@Test
	public void testEqualsObject() {
		final Cartouche c1 = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(40), 30);
		assertEquals(c, c1);
		final Cartouche c2 = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(40), 10);
		assertFalse(c.equals(c2));
	}

	@Test
	public void testExpired() {
		final Cartouche c1 = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(1).minus(Period.months(1)), 30);
		assertTrue(c1.expired());
		assertFalse(c.expired());
	}

	@Test
	public void testSolvable() {
		final Cartouche expired = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(1).minus(Period.months(1)), 30);
		final Cartouche today = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(0), 30);
		final Cartouche zero = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(30), 0);
		final Cartouche negative = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(1).minus(Period.months(1)), -10);

		assertFalse(expired.solvable());
		assertTrue(today.solvable());
		assertFalse(zero.solvable());
		assertFalse(negative.solvable());
		assertTrue(c.solvable());
		
	}
	
	@Test
	public void testUtilisablePour() {
		final Cartouche expired = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(1).minus(Period.months(1)), 30);
		final Cartouche today = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(0), 30);
		final Cartouche zero = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(30), 0);
		final Cartouche negative = new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.days(1).minus(Period.months(1)), -10);

		assertFalse(expired.utilisablePour(new Utilisation(compte, expired.getId(), 20, new Usage(new Service("foo")))));
		assertTrue(today.utilisablePour(new Utilisation(compte, today.getId(), 30, new Usage(new Service("foo")))));
		assertFalse(zero.utilisablePour(new Utilisation(compte, zero.getId(), 30, new Usage(new Service("foo")))));
		assertFalse(negative.utilisablePour(new Utilisation(compte, negative.getId(), 30, new Usage(new Service("foo")))));
		assertFalse(today.utilisablePour(new Utilisation(compte, new CartoucheId(), 10, new Usage(new Service("foo")))));
		assertFalse(today.utilisablePour(new Utilisation(new NumeroCompte(), today.getId(), 10, new Usage(new Service("foo")))));
		
	}

	@Test
	public void testCartoucheNaturalOrderParValiditeLaPlusProchePuisParUsageNaturalOrder() {
		final List<Cartouche> cartouches = Arrays.asList(
				new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.months(3), 30),
				new Cartouche(compte, new Usage(new Service("foo")), Period.months(6), 60),
				new Cartouche(compte, new Usage(new Service("bar")), Period.months(6), 100),
				new Cartouche(compte, new Usage(), Period.months(3), 10),
				new Cartouche(compte, new Usage(), Period.months(4), 70),
				new Cartouche(compte, new Usage(), Period.months(6), 80),
				new Cartouche(compte, new Usage(new Service("foo"), new Service("bar"), new Service("foobar")), Period.months(6), 40),
				new Cartouche(compte, new Usage(new Service("bar")), Period.months(3), 20)
				);

		final List<Cartouche> expected = Arrays.asList(
				new Cartouche(compte, new Usage(new Service("bar")), Period.months(3), 20),
				new Cartouche(compte, new Usage(new Service("foo"), new Service("bar")), Period.months(3), 30),
				new Cartouche(compte, new Usage(), Period.months(3), 10),
				new Cartouche(compte, new Usage(), Period.months(4), 70),
				new Cartouche(compte, new Usage(new Service("bar")), Period.months(6), 100),
				new Cartouche(compte, new Usage(new Service("foo")), Period.months(6), 60),
				new Cartouche(compte, new Usage(new Service("foo"), new Service("bar"), new Service("foobar")), Period.months(6), 40),
				new Cartouche(compte, new Usage(), Period.months(6), 80)
				);
		
		Collections.sort(cartouches);
		Assert.assertArrayEquals(expected.toArray(), cartouches.toArray());
	}

}
