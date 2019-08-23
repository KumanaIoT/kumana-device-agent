package com.kumana.iotp.shadow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceShadow {


    private Integer version;

    private Long timestamp;
    @JsonIgnore
    private Metadata metadata;
    @JsonProperty(required = true)
    private State state;

    public boolean hasReportedLocation() {
        return hasReportedState() && Objects.nonNull(getState().getReported().getLocation());
    }

    public boolean hasReportedState() {
        return null != getState() && null != getState().getReported();
    }
}
