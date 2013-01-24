package com.emo.sajou.queries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.jpa.annotations.QueryWithJpa;
import com.emo.mango.spring.jpa.support.QueryWithJpaExecutor;

@Component
@QueryWithJpa(value = CartoucheQuery.class, jpql = "select new com.emo.sajou.queries.CartoucheQuery(c.creationDate, c.id.id, c.usage.servicesAsString, c.numeroCompte.compte, c.solde.solde, c.validite) from Cartouche c")
public class CartoucheQueryExecutor extends QueryWithJpaExecutor<CartoucheQuery> {

	@Override
	protected EntityManager entityManager() {
		return em;
	}

	@PersistenceContext
	private EntityManager em;
}
