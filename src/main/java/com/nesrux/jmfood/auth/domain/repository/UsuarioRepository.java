package com.nesrux.jmfood.auth.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nesrux.jmfood.auth.domain.model.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail(String email);
}
