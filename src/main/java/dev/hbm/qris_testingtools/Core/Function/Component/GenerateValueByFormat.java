package dev.hbm.qris_testingtools.Core.Function.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service("GenerateValueByFormat")
public class GenerateValueByFormat implements FunctionProcessor {
    @Override
    public Object run(ObjectNode args) {
        String format = args.get("format").asText("");
        args.remove("format");

        for (Iterator<Map.Entry<String, JsonNode>> it = args.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            format = format.replace(key, value.asText(""));
        }

        return format;
    }
}
