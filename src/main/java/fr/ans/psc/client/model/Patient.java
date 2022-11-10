package fr.ans.psc.client.model;

import java.io.Serializable;

public class Patient implements Serializable {

    private String patientINS;
    private String patientFirstName;
    private String patientLastName;

    public String getPatientINS() {
        return patientINS;
    }

    public void setPatientINS(String patientINS) {
        this.patientINS = patientINS;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }
}
