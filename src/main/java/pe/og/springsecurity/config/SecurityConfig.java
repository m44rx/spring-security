package pe.og.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.successHandler(successHandler());
                    login.permitAll();
                })
                .sessionManagement(management -> {
                    management.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // ALWAYS - IF_REQUIRED - NEVER -STATELESS
                    management.invalidSessionUrl("/login");
                    management.maximumSessions(1).expiredUrl("/login");
                    management.sessionFixation()
                            .migrateSession();
                })
                .build();
    }   

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    
    
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");
        });
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws
    // Exception{
    // return httpSecurity
    //         .authorizeHttpRequests()
    //         .requestMatchers("/v1/index2").permitAll()
    //         .anyRequest().authenticated()
    //         .and()
    //         .formLogin(login -> login.permitAll())
    //         .httpBasic()
    //         .and()
    //         .build();
    
    // // return httpSecurity.build();
    // }
    

}
