package com.nesrux.jmfood.auth.core;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

import com.nesrux.jmfood.auth.domain.model.Usuario;

import lombok.Getter;

@Getter
public class AuthUser extends User {

	private String nome;
	private Long id;

	private static final long serialVersionUID = 1L;

	public AuthUser(Usuario usuario) {
		super(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());
		
		this.nome = usuario.getNome();
		this.id = usuario.getId();
	}

}
