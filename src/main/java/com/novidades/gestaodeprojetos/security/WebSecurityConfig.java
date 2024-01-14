package com.novidades.gestaodeprojetos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Aqui informo que é uma classe de segurança do WebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    /*
     * Método que devolve a instancia do objeto que sabe devolver o nosso padrão de
     * codificação
     * isso não tem nada a ver com o JWT
     * aqui será usado para codificar a senha do usuario gerando um hash
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Metodo padrão para configurar o nosso custom com o nosso metodo de cofificar senha
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // metodo padrão: esse metodo é obrigatorio para conseguirmos trabalhar com autenticação no login
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Metodo que tem a cconfiguração global de acessos e permissoes por rotas
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // parte padrão da configuração, por enquanto ignorar
        http
            .cors().and().csrf().disable()
            .exceptionHandling()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            /*
             * Daqui pra baixo é onde nos vamos futucar e fazer nossas validações.
              * Aqui vamos informar as rotas que não vao precisar de autenticação
              */
            .antMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/login")
            .permitAll() // informa que todos podem acessar, mas não precisa de autenticaçao.

            .anyRequest()
            .authenticated(); // Sinalizo que as demais requisiçoes devem ser autenticadas

        // Aqui informo que antes de qualquer requisição http, o sistema deve uar o
        // nosso filtro jwtAuthentication
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
