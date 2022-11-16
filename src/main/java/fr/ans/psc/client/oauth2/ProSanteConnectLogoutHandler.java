package fr.ans.psc.client.oauth2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ProSanteConnectLogoutHandler implements LogoutHandler {

    private final RestTemplate restTemplate;

    public ProSanteConnectLogoutHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        logoutFromProSanteConnect((OidcUser) auth.getPrincipal());
    }

    private void logoutFromProSanteConnect(OidcUser user) {
        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);

        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            //TODO use logger
            System.out.println("Successfully logged out from ProSanteConnect");
        } else {
            // TODO use logger
            System.out.println("Could not propagate logout to ProSanteConnect");
        }
    }
}
