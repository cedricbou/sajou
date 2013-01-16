package com.emo.sajou.domain.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {

	@Test
	public void testGetNameMustRespectCase() {
		assertEquals("ToTii", new Service("ToTii").getName());
	}
	
	@Test
	public void testServiceAreNotEqualsIfTheirNamesIsNotTheSame() {
		assertFalse(new Service("aBoulGhTT2345").equals(new Service("bOUlghtt234")));
	}

	@Test
	public void testServiceAreEqualsIfTheirNamesAreEqualsIgnoringCase() {
		assertEquals(new Service("BoulGhTT234"), new Service("bOUlghtt234"));
	}

	@Test
	public void testServiceNaturalOrderIsDoneByLiteralIgnoringCase() {
		final List<Service> unordered = Arrays.asList(
			new Service("bAaa"), new Service("bbaa"), new Service("aaaa"), new Service("Aaab"));

		final List<Service> expected = Arrays.asList(
				new Service("aaaa"), new Service("Aaab"), new Service("bAaa"), new Service("bbaa"));

		Collections.sort(unordered);
		Assert.assertArrayEquals(expected.toArray(), unordered.toArray());
	}

	@Test
	public void testToStringMustBeEqualsToNameCaseSensitive() {
		final Service s = new Service("AzertY");
		assertEquals(s.getName(), s.toString());
	}
	
	@Test
	public void testHashcodeForTwoServiceWithSameNameCaseInsensitiveShouldBeTheSame() {
		final Service s1 = new Service("AzertY");
		final Service s2 = new Service("azerty");
		
		assertEquals(s1.hashCode(), s2.hashCode());
	}

}
