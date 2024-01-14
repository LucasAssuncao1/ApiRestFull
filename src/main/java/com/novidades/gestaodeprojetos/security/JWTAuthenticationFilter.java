package com.novidades.gestaodeprojetos.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource ;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;



@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Pego o token de dentro da requisição
        String token = obterToken(request);

        // Pego o id do usuario que está dentro do token
        Optional<Long> id = jwtService.obterIdDoUsuario(token);

        // Se não achou o id, é porque o usuario nao mandou um token válido
        if (id.isPresent()) {
            // Pego o usuario dono do token pelo seu id
            UserDetails usuario = customUserDetailsService.obterUsuarioPorId(id.get());
            
            // Nesse ponto verificamos se o usuario está autenticado ou não
            // Aqui também poderiamos validar as permissões
            UsernamePasswordAuthenticationToken autenticao = 
            new UsernamePasswordAuthenticationToken(usuario, null,
                 Collections.emptyList());
            
            // Mudando a autenticaçao para a propria requisição
            autenticao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // Repasso a autenticação para o contexto do security
            // A partir de agora o spring toma conta de tudo pra mim
            SecurityContextHolder.getContext().setAuthentication(autenticao);
        }

        // Metodo padrão para filtrar as regras do usuario 
        filterChain.doFilter(request, response);
    }

    private String obterToken(HttpServletRequest request) {
        
        
        String token = request.getHeader("Authorization");

        // Verifica se veio alguma coisa sem ser espaços em brancos dentro do token
        if (!StringUtils.hasText(token)) {
            return null;
        }

        return token.substring(7);
        //Bearer 231651516518fdfdfvff

    }

}
