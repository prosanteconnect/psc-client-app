package fr.ans.psc.client.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class ShareContextController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${psc.context.sharing.api.url}")
    private String shareApiBaseUrl;

    @GetMapping("/share")
    public String getPscContext() {

        log.info("in controller");
        //TODO : if !accessToken -> auth

        HttpHeaders headers = new HttpHeaders();
        String bearer = "Bearer ";
        //TODO: use access token value
//        + accessToken.getTokenValue();
        headers.add(HttpHeaders.AUTHORIZATION, bearer);

        HttpEntity<JsonNode> entity = new HttpEntity<>(headers);
        try {
            JsonNode response = restTemplate.exchange(shareApiBaseUrl, HttpMethod.GET, entity, JsonNode.class).getBody();

            // RESPONSE LOOKUP
            JsonNode bag = response.get("bag");
            return bag.toPrettyString();

        } catch (HttpClientErrorException.NotFound e) {
            return "Aucun contexte existant n'a pu être récupéré pour cette session.";
        }

    }

    @GetMapping("/put-context")
    public String putPscContext(
    ) {
        log.info("in put controller");

        HttpHeaders headers = new HttpHeaders();
        //TODO : use token value
        String bearer = "Bearer " ;
//                + accessToken.getTokenValue();
        headers.add(HttpHeaders.AUTHORIZATION, bearer);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String jsonContext = "{\"schemaId\":\"patient-info\",\"bag\":{\"ps\":{\"nationalId\":\"899700366240\"},\"patient\":{\"patientINS\":\"2 76 05 78 455 714 30\"}}}";

        HttpEntity<String> entity = new HttpEntity<>(jsonContext, headers);

        try {
            return restTemplate.exchange(shareApiBaseUrl, HttpMethod.PUT, entity, String.class).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return "Aucun contexte existant n'a pu être récupéré pour cette session.";
        }
    }
}
