package fr.ans.psc.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class PscContext {
    @JsonProperty("psId")
    private String psId;

    @JsonProperty(value = "schemaId", required = true)
    private String schemaId;

    // must be static because Jackson ObjectNode doesn't have default constructor,
    // which leads to failures with SpringData
    @JsonProperty(value = "bag", required = true)
    private static JsonNode bag;

    public PscContext() {
    }

    public PscContext(String psId, String schemaId, JsonNode baggie) {
        this.psId = psId;
        this.schemaId = schemaId;
        bag = baggie;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public JsonNode getBag() {
        return bag;
    }

    public void setBag(JsonNode baggie) {
        bag = baggie;
    }
}
