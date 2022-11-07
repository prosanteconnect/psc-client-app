package fr.ans.psc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(a -> {
                            try {
                                a
                                        .antMatchers("/", "/error", "/webjars/**", "/login", "/user").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .oauth2Login().loginPage("/login")
                                        .userInfoEndpoint()
                                        .userService(userService);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                ).logout(l -> l.logoutSuccessUrl("/").permitAll());
    }
}
