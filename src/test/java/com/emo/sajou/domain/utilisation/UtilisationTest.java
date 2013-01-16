package com.emo.sajou.domain.utilisation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.emo.sajou.domain.cartouche.CartoucheId;
import com.emo.sajou.domain.commons.Service;
import com.emo.sajou.domain.commons.Usage;
import com.emo.sajou.domain.compte.NumeroCompte;

public class UtilisationTest {
	private final NumeroCompte compte = new NumeroCompte();
	private final CartoucheId cartoucheId = new CartoucheId();
	private final Usage usage = new Usage();
	private final Utilisation u = new Utilisation(compte, cartoucheId, 30, usage);

	@Test
	public void testHashCode() {
		final Utilisation u0 = new Utilisation(compte, cartoucheId, 30, usage);
		assertEquals(u0.hashCode(), u.hashCode());

		final Utilisation u1 = new Utilisation(compte, cartoucheId, 30, new Usage(new Service("foo")));
		final Utilisation u2 = new Utilisation(compte, cartoucheId, 50, usage);
		final Utilisation u3 = new Utilisation(compte, new CartoucheId(), 30, usage);
		final Utilisation u4 = new Utilisation(new NumeroCompte(), cartoucheId, 30, usage);

		assertFalse(u1.hashCode() == u.hashCode());
		assertFalse(u2.hashCode() == u.hashCode());
		assertFalse(u3.hashCode() == u.hashCode());
		assertFalse(u4.hashCode() == u.hashCode());
		
	}

	@Test
	public void testGetNumeroCompte() {
		assertEquals(compte, u.getNumeroCompte());
	}

	@Test
	public void testGetCartoucheId() {
		assertEquals(cartoucheId, u.getCartoucheId());
	}

	@Test
	public void testGetUsage() {
		assertEquals(usage, u.getUsage());
	}

	@Test
	public void testGetMontant() {
		assertEquals(30, u.getMontant());
	}

	@Test
	public void testUtilisationEgalite() {
		final Utilisation u1 = new Utilisation(compte, cartoucheId, 30, usage);
		assertEquals(u, u1);
	}

	@Test
	public void testUtilisationNonEgalite() {
		final Utilisation u1 = new Utilisation(compte, cartoucheId, 30, new Usage(new Service("foo")));
		final Utilisation u2 = new Utilisation(compte, cartoucheId, 50, usage);
		final Utilisation u3 = new Utilisation(compte, new CartoucheId(), 30, usage);
		final Utilisation u4 = new Utilisation(new NumeroCompte(), cartoucheId, 30, usage);
		
		assertFalse(u.equals(u1));
		assertFalse(u.equals(u2));
		assertFalse(u.equals(u3));
		assertFalse(u.equals(u4));
	}

}
