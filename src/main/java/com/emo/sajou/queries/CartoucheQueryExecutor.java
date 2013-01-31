package com.emo.sajou.queries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.jpa.annotations.Jpql;
import com.emo.mango.spring.jpa.annotations.QueryParams;
import com.emo.mango.spring.jpa.support.JpqlExecutor;

@Component
@Jpql(jpql = "from Cartouche c where c.numeroCompte.compte = ?", clazz=CartoucheQuery.class)
@QueryParams("compte")
public class CartoucheQueryExecutor extends JpqlExecutor<CartoucheQuery> {

	@Override
	protected EntityManager entityManager() {
		return em;
	}

	@PersistenceContext
	private EntityManager em;
}
