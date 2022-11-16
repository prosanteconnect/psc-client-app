package fr.ans.psc.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DossierPatientController {

    @GetMapping("/oidc/redirect")
    public String showRedirectPage() {
        return "dossier-patient";}

    @GetMapping("/dossier-patient")
    public String showPatientForm() {
        return "dossier-patient";
    }
}
