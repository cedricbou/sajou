package com.emo.sajou.domain.commons;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class Usage implements Comparable<Usage> {
	private static final Service SERVICE_ALL = new Service("*all");
	public static final Usage ALL = new Usage(SERVICE_ALL);

	@Transient
	private /* should be final */ SortedSet<Service> services;

	@Column(nullable=false)
	private /* should be final */ String servicesAsString = "";
		
	public Usage(final Service... services) {
		init(services);
	}
	
	/*
	 * JPA does not like varargs constructor and require an empty constructor.
	 * Although the empty constructor is not compatible with the program invariants.
	 * So the trick here, is that the default constructor creates a ALL service usage.
	 * If it is used as a value object, then, it correctly create a ALL service usage,
	 * if JPA uses this constructor, it will construct an ALL service, but JPA should
	 * override the services with the content of the database.
	 * 
	 * Tricky JPA/Object integration :)
	 */
	protected Usage() {
		init(new Service[] {});
	}
	
	public Usage(final Set<Service> services, final Service service) {
		final SortedSet<Service> sortedService = new TreeSet<Service>(services);
		sortedService.add(service);
		this.servicesAsString = StringUtils.join(sortedService, "+");
	}
	
	private void init(final Service[] services) {
		if(services.length > 0) {
			final SortedSet<Service> sortedService = new TreeSet<Service>(Arrays.asList(services));
			this.servicesAsString = StringUtils.join(sortedService, "+");
		}
		else {
			this.servicesAsString = SERVICE_ALL.toString();
		}
	}
	
	@Deprecated
	protected String getServicesAsString() {
		return servicesAsString;
	}

	private void createServicesFromString() {
		if(services == null) {
			this.services = new TreeSet<Service>();
			for(final String service : StringUtils.split(servicesAsString, "+")) {
				this.services.add(new Service(service));
			}
		}
	}

	public Set<Service> getServices() {
		createServicesFromString();
		return Collections.unmodifiableSortedSet(services);
	}

	public boolean appliesToAll() {
		return getServices().contains(SERVICE_ALL);
	}
	
	public boolean isSubsetOf(final Usage usage) {
		if(usage.appliesToAll()) {
			return true;
		}
		
		boolean existsInSet = true;
		
		for(final Service service : this.getServices()) {
			existsInSet &= usage.getServices().contains(service);
		}
		
		return existsInSet;
	}

	public Usage withService(final Service service) {
		return new Usage(getServices(), service);
	}
	
	public String id() {
		return servicesAsString;
	}
	
	@Override
	public int hashCode() {
		return new Long(134 + servicesAsString.toLowerCase().hashCode()).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Usage
				&& this.servicesAsString.toLowerCase().equals(((Usage)obj).servicesAsString.toLowerCase());
	}
	
	@Override
	public String toString() {
		return servicesAsString;
	}

	@Override
	public int compareTo(Usage o) {
		final Usage c1 = this;
		final Usage c2 = o;
		
		if (c1.appliesToAll()) {
			return 1;
		}
		if (c2.appliesToAll()) {
			return -1;
		}
		
		final int sizeSort = c1.getServices().size() - c2.getServices().size();
		
		if(sizeSort == 0) {
			return c1.servicesAsString.toLowerCase().compareTo(c2.servicesAsString.toLowerCase());
		}
		
		return sizeSort;
	}
}
