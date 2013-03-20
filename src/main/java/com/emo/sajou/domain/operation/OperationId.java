package com.emo.sajou.domain.operation;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.emo.mango.annotations.Final;

@Embeddable
public class OperationId {
	
	@Column(name = "operationId", nullable=false)
	private @Final String id;

	public OperationId() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof OperationId
				&& this.id.equals(((OperationId) obj).id);
	}
	
	@Override
	public int hashCode() {
		return new Long(309 + id.hashCode()).hashCode();
	}

	@Override
	public String toString() {
		return "operation-" + id.toString();
	}}
