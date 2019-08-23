package com.kumana.iotp.shadow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.kumana.iotp.AlertModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class CreateJsonSchema {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JsonSchemaGenerator jsonSchemaGenerator;

    public CreateJsonSchema() {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        jsonSchemaGenerator = new JsonSchemaGenerator(MAPPER);
    }

    public String generateReported(final HashMap<String, String> sensorvalues) throws JsonProcessingException {
        final DeviceShadow deviceShadow = new DeviceShadow();
        final State state = new State();
        deviceShadow.setState(state);
        final Reported reported = new Reported();
        state.setReported(reported);
        final Location location = new Location();
        location.setLat(123.45);
        location.setLon(344.32);
        reported.setLocation(location);
        final Latest latest = new Latest();
        latest.setTimestamp(System.currentTimeMillis());

        latest.setSensors(sensorvalues);
//        final HashMap<String, String> hashMap2 = new HashMap<>();
//        for (String key : sensorvalues.keySet()) {
//            hashMap2.put(key, "on");
//        }
//        latest.setActuators(hashMap2);
        reported.setLatest(latest);
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(deviceShadow);
    }


    public String generateAlerts(final List<AlertModel> alertList) throws JsonProcessingException {
        final DeviceShadow deviceShadow = new DeviceShadow();
        final State state = new State();
        deviceShadow.setState(state);
        final Reported reported = new Reported();
        state.setReported(reported);
        final Location location = new Location();
        location.setLat(123.45);
        location.setLon(344.32);
        reported.setLocation(location);
        final Latest latest = new Latest();
        latest.setTimestamp(System.currentTimeMillis());
        reported.setLatest(latest);
        reported.setAlerts(alertList);
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(deviceShadow);
    }


}
