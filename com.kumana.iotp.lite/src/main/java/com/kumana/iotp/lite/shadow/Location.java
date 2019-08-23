package com.kumana.iotp.lite.shadow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

    @JsonProperty(required = true)
    private double lat;

    @JsonProperty(required = true)
    private double lon;

    private Double alt;
    private Double spd;
    private Double hdg;
    private Integer nSats;
    @JsonProperty(required = true)
    private Long timestamp;

    public Location(final double lat, final double lon) {
        super();
        this.lat = lat;
        this.lon = lon;
    }

    public Location(final com.kumana.iotp.shadow.model.Location modelLocation) {
        super();
        this.lat = modelLocation.getLatitude();
        this.lon = modelLocation.getLongitude();
        this.alt = modelLocation.getAltitude();
        this.spd = modelLocation.getSpeed();
        this.hdg = modelLocation.getHeading();
        this.nSats = modelLocation.getNumberOfSatellites();
        this.timestamp = modelLocation.getTimestamp();
    }

    public Integer getnSats() {
        return nSats;
    }

    public Location setnSats(final Integer nSats) {
        this.nSats = nSats;
        return this;
    }


}
