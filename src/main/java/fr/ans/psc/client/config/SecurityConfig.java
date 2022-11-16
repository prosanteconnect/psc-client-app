package fr.ans.psc.client.config;

import fr.ans.psc.client.oauth2.ProSanteConnectLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ProSanteConnectLogoutHandler proSanteConnectLogoutHandler;

    SecurityConfig(ProSanteConnectLogoutHandler proSanteConnectLogoutHandler) {
        this.proSanteConnectLogoutHandler = proSanteConnectLogoutHandler;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/dossier-patient").authenticated();

        http
                .oauth2Login()
                    .and()
                .logout()
                .addLogoutHandler(proSanteConnectLogoutHandler)
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                    .and()
                .cors().disable()
                .anonymous().disable();
        return http.build();
    }
}
