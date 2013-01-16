package com.emo.sajou.domain.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class UsageTest {

	@Test public void testCreatingUsageWithDefaultConstructorCreateAUSageForAll() {
		final Usage u = new Usage();
		assertTrue(u.appliesToAll());
	}
	
	@Test public void testCreatingUsageWithTheStaticAllUsage() {
		final Usage u = Usage.ALL;
		assertTrue(u.appliesToAll());
	}
	
	@Test public void testCreatingUsageWithTheSpecialAllService() {
		final Usage u = new Usage(new Service("*all"));
		assertTrue(u.appliesToAll());
	}
	
	@Test public void testAnyServicesIsASubsetOfAll() {
		final Usage u1 = new Usage(new Service("foo"));
		final Usage u2 = new Usage(new Service("bar"));
		
		assertTrue(u1.isSubsetOf(Usage.ALL));
		assertTrue(u2.isSubsetOf(Usage.ALL));
	}
	
	@Test public void testAnySetOfServicesIsASubsetOfAll() {
		final Usage u = new Usage(new Service("foo1"), new Service("foo2"), new Service("bar"));
		assertTrue(u.isSubsetOf(Usage.ALL));
	}

	@Test public void testIdenticalSetsAreSubsetsOfEachOtherCaseInsensitive() {
		final Usage u1 = new Usage(new Service("foo"), new Service("bAr"));
		final Usage u2 = new Usage(new Service("fOo"), new Service("bar"));
		
		assertTrue(u1.isSubsetOf(u2));
		assertTrue(u2.isSubsetOf(u1));
	}
	
	@Test public void testIdenticalServicesCaseInsensitiveWithinAUsageAreConsideredAsOne() {
		final Usage u = new Usage(new Service("fOo"), new Service("bAr"), new Service("fOO"), new Service("BAr"));
		final Usage expected = new Usage(new Service("foo"), new Service("bar"));
		
		assertEquals(2, u.getServices().size());
		assertEquals(expected, u);
	}
	
	@Test public void testUsageForAllIsASubsetOfAnotherUsageForAll() {
		final Usage u = new Usage(new Service("*all"));
		assertTrue(u.isSubsetOf(Usage.ALL));
	}
	
	@Test public void testUsageNotForAllServicesDoesNotAppliesToAll() {
		final Usage u = new Usage(new Service("foo"), new Service("bar"));
		assertFalse(u.appliesToAll());
	}
	
	@Test public void testUsageWithSeveralServicesAreEqualsWhenTheyHaveTheSameServicesCaseInsensitiveDeclaredInAnyOrder() {
		final Usage u1 = new Usage(new Service("foo"), new Service("bar"), new Service("foobar"));
		final Usage u2 = new Usage(new Service("bAr"), new Service("foOBAr"), new Service("FOO"));
			
		assertEquals(u1, u2);
	}

	@Test public void testUsageWithSeveralServicesHaveTheSameHashcodeWhenTheyHaveTheSameServicesCaseInsensitiveDeclaredInAnyOrder() {
		final Usage u1 = new Usage(new Service("foo"), new Service("bar"), new Service("foobar"));
		final Usage u2 = new Usage(new Service("bAr"), new Service("foOBAr"), new Service("FOO"));
		
		assertEquals(u1.hashCode(), u2.hashCode());
	}
	
	@Test public void testUsagesAreNotEqualsIfTheyDoNotHaveTheSameServices() {
		final Usage u1 = new Usage(new Service("foo"), new Service("bar"), new Service("foobar"));
		final Usage u2 = new Usage(new Service("foOBAr"), new Service("FOO"));
		
		assertFalse(u1.equals(u2));
	}

	@Test public void testUsagesDoNotHaveTheSameHashcodesIfTheyDoNotHaveTheSameServices() {
		final Usage u1 = new Usage(new Service("foo"), new Service("bar"), new Service("foobar"));
		final Usage u2 = new Usage(new Service("foOBAr"), new Service("FOO"));
		
		assertFalse(u1.hashCode() == u2.hashCode());
	}
	
	@Test public void testUsageNaturalOrderIsByAscNumberOfServicesThenAllUsagesThenStringNaturalOrderCaseInsensitive() {
		final Usage[] unordered = new Usage[] {
				new Usage(new Service("Foo")), new Usage(new Service("bar")), new Usage( new Service("foo2")), 
				new Usage(new Service("Bar2")), new Usage(new Service("zoo"), new Service("zar")),
				new Usage(new Service("foo"), new Service("bAR")) };
		
		final Usage[] expected = new Usage[] {
				new Usage(new Service("bar")), new Usage(new Service("Bar2")),
				new Usage(new Service("Foo")), new Usage( new Service("foo2")),
				new Usage(new Service("foo"), new Service("bAR")),
				new Usage(new Service("zoo"), new Service("zar")) };

		Arrays.sort(unordered);
				
		Assert.assertArrayEquals(expected, unordered);
	}

	@Test public void testToStringReturnsTheOrderedServiceNameJoinedByPlus() {
		final Usage u = new Usage(new Service("Foo"), new Service("bAr"));
		assertEquals("bAr+Foo", u.toString());
	}
}
