package fr.ans.psc.client.config;

import fr.ans.psc.client.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder.builder;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService userService;

//    @Autowired
//    private ClientRegistrationRepository clientRegistrationRepository;
//
//    @Autowired
//    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
//
//    @Autowired
//    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
//
//    @Autowired
//    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // HTTP SECURITY
        http
                .authorizeRequests()
                .antMatchers("/", "/webjars/**",  "/error", "logout").permitAll()
                .anyRequest().authenticated()
                    .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                    .and()
                .csrf()
                .ignoringAntMatchers("/login", "/logout")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .oauth2Login()
                .loginPage("/")
                .defaultSuccessUrl("/")
//                .clientRegistrationRepository(clientRegistrationRepository)
//                .authorizedClientService(oAuth2AuthorizedClientService)
//                .authorizationEndpoint()
//                .baseUri(clientRegistrationRepository.findByRegistrationId("prosanteconnect").getProviderDetails().getAuthorizationUri())
//                .authorizationRequestRepository(authorizationRequestRepository)
//                    .and()
//                .tokenEndpoint()
//                .accessTokenResponseClient(accessTokenResponseClient)
//                    .and()
                .userInfoEndpoint()
                .userService(userService);
    }
}
