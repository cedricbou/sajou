package com.emo.sajou.domain.cartouche;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emo.sajou.domain.compte.NumeroCompte;

public interface Cartouches extends JpaRepository<Cartouche, Long> {
	
	public List<Cartouche> findByNumeroCompte(final NumeroCompte numero);
}
