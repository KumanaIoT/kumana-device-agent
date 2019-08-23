package com.kumana.iotp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by priyamal on 7/18/17.
 */
@Data
@NoArgsConstructor
public class SensorReadingModel {

    public HashMap<String, String> reading = new HashMap<>();
    public String key;

    public void addReading(String key, String value) {
        reading.put(key, value);
    }

    public String getReading(String key) {
        String readingValue = reading.get(key);
        if (readingValue == null) {
            return "";
        }
        return readingValue;
    }

}