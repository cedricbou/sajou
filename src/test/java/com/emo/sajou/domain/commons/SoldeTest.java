package com.emo.sajou.domain.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SoldeTest {

	@Test
	public void testUnSoldeCreePourUnMontantRetourneCeMontant() {
		final Solde solde = new Solde(34);
		assertEquals(34, solde.getSolde());
	}
	
	@Test
	public void testDeuxSoldesAvecLeMemeMontantSontEgaux() {
		final Solde s1 = new Solde(34);
		final Solde s2 = new Solde(34);
		
		assertEquals(s1, s2);
	}
	
	@Test
	public void testDeuxSoldesAvecDesMontantsDifferentsNeSontPasEgaux() {
		final Solde s1 = new Solde(23);
		final Solde s2 = new Solde(35);
		
		assertFalse(s1.equals(s2));
	}
	
	@Test
	public void testDeuxSoldesAvecLeMemeMontantOntLeMemeHashCode() {
		assertEquals(new Solde(34).hashCode(), new Solde(34).hashCode());
	}
	
	@Test
	public void testDeuxSoldesAvecDeuxMontantsDifferentsOntDesHashcodeDifferents() {
		assertFalse(new Solde(34).hashCode() == new Solde(18).hashCode());
	}
	
	@Test
	public void testDeduireUnMontantRenvoieUnNouveauSoldeDuMontantDeduit() {
		final Solde solde = new Solde(34);
		assertEquals(new Solde(20), solde.deduire(14));
	}
	
	@Test
	public void testAjouterUnMontantRenvoieUnNouveauSoldeAuMontantAjoute() {
		final Solde solde = new Solde(34);
		assertEquals(new Solde(44), solde.ajouter(10));
	}
	
	@Test
	public void testToStringRenvoieLeSoldeSansEspaces() {
		assertEquals("34", new Solde(34).toString());
	}

}
