package fr.ans.psc.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class ShareContextController {

    private final String OAUTH2_PROVIDER = "github";
    private final String PROSANTECONNECT_PROVIDER = "prosanteconnect";

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/share")
    public ResponseEntity<String> getPscContext(@RegisteredOAuth2AuthorizedClient(
//            OAUTH2_PROVIDER
            PROSANTECONNECT_PROVIDER
    ) OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        //TODO : if !accessToken -> auth
        //TODO : try get ctx
        //TODO : if invalid token -> refresh token
        //TODO: retry get ctx
        //TODO return ctx.toString
        return null;
    }

    @PostMapping("/share")
    public ResponseEntity<String> putPscContext(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
