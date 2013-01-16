package com.emo.sajou.domain.commons;


public class Service implements Comparable<Service> {
	private /* should be final */ String name;

	public Service(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Service
				&& this.name.equalsIgnoreCase(((Service) obj).name);
	}
	
	@Override
	public int hashCode() {
		return new Long(14 + name.toLowerCase().hashCode()).hashCode();
	}
	
	@Override
	public int compareTo(Service o) {
		return this.name.toLowerCase().compareTo(o.name.toLowerCase());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Deprecated
	protected Service() {
		
	}
}
