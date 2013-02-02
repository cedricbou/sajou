package com.emo.sajou.queries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.emo.mango.spring.query.annotations.Jpql;
import com.emo.mango.spring.query.annotations.QueryParams;
import com.emo.mango.spring.query.support.JpqlExecutor;

@Component
@Jpql(jpql = "from Cartouche c where c.numeroCompte.compte = ?", clazz=CartoucheJpql.class)
@QueryParams("compte")
public class CartoucheJpqlExecutor extends JpqlExecutor<CartoucheJpql> {

	@Override
	protected EntityManager entityManager() {
		return em;
	}

	@PersistenceContext
	private EntityManager em;
}
