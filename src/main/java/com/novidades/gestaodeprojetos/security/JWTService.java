package com.novidades.gestaodeprojetos.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.novidades.gestaodeprojetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTService {
    
    private static final String chavePrivadaJWT ="secretKey";

    /**
     *  Metodo para gerar o token JWT
     * @param authentication autenticação do usuario
     * @return
     */
    public String gerarToken (Authentication authentication){

        // 1 dia em milisegundos
        // Aqui pode variar de acordo com a necessidade
        int tempoExpiracao = 86400000;

        //Aqui estou criando uma data de expiração para o token com base no tempo de expiração
        // ele pega  a data atual e soma mais um dia em milisegundos
        Date dataExpiracao = new Date(new Date().getTime() + tempoExpiracao);

        // Aqui pegamos o usuario atual da autenticaçao
        Usuario usuario = (Usuario)authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS512, chavePrivadaJWT)
                .compact();
                       
        
    }

    /**
     * Metodo para retornar o id do usuario dono do token
     * @param token token do usuario
     * @return id do usuario 
     */
    public Optional<Long> obterIdDoUsuario (String token){

        try {
            // Retorna as permissões do token
            Claims claims = parse(token).getBody();

            // Retorna o id de dentro do token se encontrar, caso contrario retorna null
            return Optional.ofNullable(Long.parseLong(claims.getSubject()));

        } catch (Exception e) {
            // senão encontrar nada, devolve um optional null
            return Optional.empty();
        }
    }


    // Metodo que sabe descobrir de dentro do token com base na chave privada qual as permissões do usuario
    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(chavePrivadaJWT).parseClaimsJws(token);
    }


}
