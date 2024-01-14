package com.novidades.gestaodeprojetos.service;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.novidades.gestaodeprojetos.model.Usuario;
import com.novidades.gestaodeprojetos.repository.UsuarioRepository;
import com.novidades.gestaodeprojetos.security.JWTService;
import com.novidades.gestaodeprojetos.view.usuario.LoginResponse;

@Service
public class UsuarioService {

    private static final String headerPrefix = "Bearer ";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> obterTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obterPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionar(Usuario usuario) {
        usuario.setId(null);

        if (obterPorEmail(usuario.getEmail()).isPresent()) {
            throw new InputMismatchException("Já existe um usuario cadastrado com o email: " + usuario.getEmail());

        }

        String senha = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senha);

        return usuarioRepository.save(usuario);

    }

    public LoginResponse logar (String email, String senha){

        // Aqui a autenticacao acontece magicamente
        Authentication autenticacao = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha, Collections.emptyList()));
        
            // Aqui eu passo a nova autenticação para o Spring Security cuidar pra mim 
            SecurityContextHolder.getContext().setAuthentication(autenticacao);

            // Gera o token do usuario para devolver a ele
            // Bearer afaf5a5faf5af4a
            String token = headerPrefix + jwtService.gerarToken(autenticacao);

            Usuario usuario = usuarioRepository.findByEmail(email).get();

            return new LoginResponse(token, usuario);
    }
}
