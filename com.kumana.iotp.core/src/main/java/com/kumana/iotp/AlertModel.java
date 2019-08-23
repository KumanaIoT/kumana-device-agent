package com.kumana.iotp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertModel {


    @JsonProperty(required = true)
    private String primaryCode;
    private String secondaryCode;
    private Map<String, String> variables;
    @JsonProperty(required = true)
    private AlertStatus alertStatus;
    @JsonProperty(required = true)
    private long statusChangeTimestamp;


    public enum AlertStatus {
        ACTIVE,
        RESOLVED,
        INFORMATIONAL
    }

    @JsonIgnore
    public boolean isActive() {
        return null != alertStatus && alertStatus.equals(AlertStatus.ACTIVE);
    }

    @JsonIgnore
    public boolean isResolved() {
        return null != alertStatus && alertStatus.equals(AlertStatus.RESOLVED);
    }

    @JsonIgnore
    public boolean isInformational() {
        return null != alertStatus && alertStatus.equals(AlertStatus.INFORMATIONAL);
    }

    @Override
    public boolean equals(Object o) {
        AlertModel alert = (AlertModel) o;
        return primaryCode.equals(alert.primaryCode);
    }

    @Override
    public int hashCode() {
        return primaryCode.hashCode();
    }


}
