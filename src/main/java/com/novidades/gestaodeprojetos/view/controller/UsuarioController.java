package com.novidades.gestaodeprojetos.view.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novidades.gestaodeprojetos.model.MensagemEmail;
import com.novidades.gestaodeprojetos.model.Usuario;
import com.novidades.gestaodeprojetos.service.EmailService;
import com.novidades.gestaodeprojetos.service.UsuarioService;
import com.novidades.gestaodeprojetos.view.usuario.LoginRequest;
import com.novidades.gestaodeprojetos.view.usuario.LoginResponse;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Usuario> obterTodos() {
        return usuarioService.obterTodos();
    }

    @GetMapping("{id}")
    public Optional<Usuario> obterPorId(@PathVariable Long id) {
        return usuarioService.obterPorId(id);
    }

    @PostMapping
    public Usuario adicionar(@RequestBody Usuario usuario) {
        return usuarioService.adicionar(usuario);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return usuarioService.logar(request.getEmail(), request.getSenha());

    }

    @PostMapping("/email")
    public String enviarEmail(@RequestBody MensagemEmail email) {
        try {
            emailService.enviar(email);
            return "Deu certo";

        } catch (Exception e) {
            e.printStackTrace();
            return "Deu ruim";
        }
    }

}
