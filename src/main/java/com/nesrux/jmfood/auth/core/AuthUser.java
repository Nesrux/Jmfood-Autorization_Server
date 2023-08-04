package com.nesrux.jmfood.auth.core;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.nesrux.jmfood.auth.domain.model.Usuario;

import lombok.Getter;

@Getter
public class AuthUser extends User {

	private String nome;
	private Long id;

	private static final long serialVersionUID = 1L;

	public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);

		this.nome = usuario.getNome();
		this.id = usuario.getId();
	}

}
