package fr.ans.psc.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ans.psc.client.model.Patient;
import fr.ans.psc.client.model.Ps;
import fr.ans.psc.client.model.PscContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class ShareContextController {

    private Ps ps = new Ps();
    private Patient patient = new Patient();

    private final String GITHUB_PROVIDER = "github";
    private final String PROSANTECONNECT_PROVIDER = "prosanteconnect";

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/share")
    public String getPscContext(
            @RegisteredOAuth2AuthorizedClient(
            GITHUB_PROVIDER
//            PROSANTECONNECT_PROVIDER
    ) OAuth2AuthorizedClient authorizedClient
    ) {
        log.info("in controller");
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        //TODO : if !accessToken -> auth


        log.info("access token : {}", accessToken.getTokenValue());
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://prosanteconnect.share-context.henix.asipsante.fr/psc-context-sharing/api/share";

        HttpHeaders headers = new HttpHeaders();
        String bearer = "Bearer " + accessToken.getTokenValue();
        headers.add(HttpHeaders.AUTHORIZATION, bearer);

        HttpEntity<JsonNode> entity = new HttpEntity<>(headers);
        try {
            JsonNode response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, JsonNode.class).getBody();

            // INTROSPECT RESPONSE
            JsonNode bag = response.get("bag");
            JsonNode psNode = bag.get("ps");
            JsonNode patientNode = bag.get("patient");
            String psNationalId = psNode.get("nationalId").textValue();
            String patientINS = patientNode.get("patientINS").textValue();

            return bag.toPrettyString();
        } catch (HttpClientErrorException.NotFound e) {
            return "Aucun contexte existant n'a pu être récupéré pour cette session.";
        }

    }

    @GetMapping("/put-context")
    public String putPscContext(@RegisteredOAuth2AuthorizedClient(
            GITHUB_PROVIDER
//            PROSANTECONNECT_PROVIDER
    ) OAuth2AuthorizedClient authorizedClient) {
        log.info("in put controller");
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://prosanteconnect.share-context.henix.asipsante.fr/psc-context-sharing/api/share";

        HttpHeaders headers = new HttpHeaders();
        String bearer = "Bearer " + accessToken.getTokenValue();
        headers.add(HttpHeaders.AUTHORIZATION, bearer);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String jsonContext = "{\"schemaId\":\"patient-info\",\"bag\":{\"ps\":{\"nationalId\":\"899700366240\"},\"patient\":{\"patientINS\":\"2 76 05 78 455 714 30\"}}}";

        HttpEntity<String> entity = new HttpEntity<>(jsonContext, headers);

        try {
            restTemplate.exchange(baseUrl, HttpMethod.PUT, entity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            return "Aucun contexte existant n'a pu être récupéré pour cette session.";
        }

        return "ok";
//        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
