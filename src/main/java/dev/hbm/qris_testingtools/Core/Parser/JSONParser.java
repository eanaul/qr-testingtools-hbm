package dev.hbm.qris_testingtools.Core.Parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    public Map<String, Object> unpack(JsonNode node) {
        Map<String, Object> tempF = new HashMap<>();

        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            if (value.isObject()) {
                tempF.put(key, unpack(value));
            } else if (value.isArray()) {
                List<Object> arrayValues = new ArrayList<>();
                for (JsonNode arrayElement : value) {
                    if (arrayElement.isObject()) {
                        arrayValues.add(unpack(arrayElement));
                    } else {
                        arrayValues.add(arrayElement);
                    }
                }
                tempF.put(key, arrayValues);
            } else {
                tempF.put(key, value);
            }
        });

        return tempF;
    }
}
