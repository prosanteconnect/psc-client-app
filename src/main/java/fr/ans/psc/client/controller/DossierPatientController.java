package fr.ans.psc.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DossierPatientController {

    private String nationalId = "";
    private String patientINS = "";


    @GetMapping("/dossier-patient")
    public String showPatientForm() {
        return "dossier-patient";
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPatientINS() {
        return patientINS;
    }

    public void setPatientINS(String patientINS) {
        this.patientINS = patientINS;
    }
}
