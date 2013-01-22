package com.emo.sajou.commands;

import net.sf.oval.constraint.Length;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class FooBar {

	@Length(max=2)
	public final String foo;

	public final String bar;
	
	@JsonCreator
	public FooBar(final @JsonProperty("foo") String foo, final @JsonProperty("bar") String bar) {
		this.foo = foo;
		this.bar = bar;
	}
	
	@Override
	public String toString() {
		return "foo: " + foo + "; bar: " + bar;
	}
}
