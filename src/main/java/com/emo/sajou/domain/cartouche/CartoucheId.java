package com.emo.sajou.domain.cartouche;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CartoucheId {
	
	@Column(name = "cartoucheId", nullable=false)
	private /* should be final */ String id;

	public CartoucheId() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CartoucheId
				&& this.id.equals(((CartoucheId) obj).id);
	}
	
	@Override
	public int hashCode() {
		return new Long(10 + id.hashCode()).hashCode();
	}

	@Override
	public String toString() {
		return "cartouche-" + id.toString();
	}}
