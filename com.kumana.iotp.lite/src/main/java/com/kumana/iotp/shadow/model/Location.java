package com.kumana.iotp.shadow.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Location {

    private String id;
    private Double longitude;
    private Double latitude;
    private String address;
    private String postcode;
    private Double altitude;
    private Double speed;
    private Double heading;
    private Integer numberOfSatellites;
    private Long timestamp;
    private Map<String, String> attributes = new HashMap<>();

    public Location(final double longitude, final double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(final com.kumana.iotp.lite.shadow.Location shadowLocation) {
        this.latitude = shadowLocation.getLat();
        this.longitude = shadowLocation.getLon();
        this.altitude = shadowLocation.getAlt();
        this.speed = shadowLocation.getSpd();
        this.heading = shadowLocation.getHdg();
        this.numberOfSatellites = shadowLocation.getnSats();
        this.timestamp = shadowLocation.getTimestamp();
    }

    public boolean hasId() {
        return null != id;
    }

}
