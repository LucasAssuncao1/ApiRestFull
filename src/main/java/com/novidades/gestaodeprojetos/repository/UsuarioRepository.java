package com.novidades.gestaodeprojetos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novidades.gestaodeprojetos.model.Usuario;


public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    
    Optional<Usuario> findByEmail (String email);
}
