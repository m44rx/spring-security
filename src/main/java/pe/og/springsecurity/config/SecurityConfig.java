package pe.og.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Configuracion usando lambdas, lo recomienda spring

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    // Manejador de Autentificacion exitoso, redirigir, tiene otras opciones como
                    // que hacer en caso de error
                    login.successHandler(successHandler());
                    login.permitAll();
                })
                .sessionManagement(management -> {
                    management.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // ALWAYS - IF_REQUIRED - NEVER -STATELESS

                })
                .build();
    }

    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/index");
        });
    }

    /*
     * -> Configuration One <-
     * 
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws
     * Exception{
     * return httpSecurity
     * // Cross-Site Request Forgery - csrf - formulario a través del navegador, por
     * defecto activo que resuleve vulnerabilidades - .csrf().disable para
     * desactivar
     * .authorizeHttpRequests()
     * // No necesitara autorizacion en requestMatchers + permitAll
     * // Solicitudes que apunten especificamnt a este path, se les permite a todos
     * .requestMatchers("/v1/index2").permitAll()
     * // Solicitudes a otros path deben ser autenticadas
     * .anyRequest().authenticated()
     * .and()
     * // Permitir a todos acceder al formulario
     * .formLogin(login -> login.permitAll())
     * .build();
     * 
     * return httpSecurity.build();
     * }
     */

}
