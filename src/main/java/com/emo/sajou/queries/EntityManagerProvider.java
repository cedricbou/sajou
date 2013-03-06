package com.emo.sajou.queries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class EntityManagerProvider implements com.emo.mango.spring.query.support.EntityManagerProvider {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public EntityManager get() {
		return em;
	}
}
