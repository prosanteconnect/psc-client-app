package fr.ans.psc.client.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder.builder;

@Configuration
public class Oauth2ClientRegistrationConfiguration {

    private final String PROSANTECONNECT = "prosanteconnect";

    @Bean
    public OAuth2ClientProperties properties() {
        return new OAuth2ClientProperties();
    }

    @Bean
    public ClientRegistration clientRegistration(OAuth2ClientProperties properties) {
        OAuth2ClientProperties.Provider provider = properties.getProvider().get(PROSANTECONNECT);
        OAuth2ClientProperties.Registration registration = properties.getRegistration().get(PROSANTECONNECT);

        return ClientRegistration.withRegistrationId(PROSANTECONNECT)
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .authorizationGrantType(new AuthorizationGrantType(registration.getAuthorizationGrantType()))
                .redirectUri(registration.getRedirectUri())
                .scope(registration.getScope())
                .clientAuthenticationMethod(new ClientAuthenticationMethod(registration.getClientAuthenticationMethod()))
                .tokenUri(provider.getTokenUri())
                .authorizationUri(provider.getAuthorizationUri())
                .userInfoUri(provider.getUserInfoUri())
                .userNameAttributeName(provider.getUserNameAttribute())
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration clientRegistration) {
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository)
    {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceOAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService)
    {
        var authorizedClientProvider = builder().authorizationCode().build();
        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }
}
