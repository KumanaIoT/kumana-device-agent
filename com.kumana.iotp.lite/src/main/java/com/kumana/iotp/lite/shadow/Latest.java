package com.kumana.iotp.lite.shadow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Latest {

    private HashMap<String, String> sensors;
    private HashMap<String, String> actuators;
    @JsonProperty(required = true)
    private Long timestamp;

}
