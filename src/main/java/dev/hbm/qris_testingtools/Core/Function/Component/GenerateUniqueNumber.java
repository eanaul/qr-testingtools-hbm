package dev.hbm.qris_testingtools.Core.Function.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component("GenerateUniqueNumber")
public class GenerateUniqueNumber implements FunctionProcessor {
    @Override
    public Object run(ObjectNode args) {
        String size = args.get("size").asText("");
        return generateRandomNumber(Integer.parseInt(size));
    }

    public String generateRandomNumber(int size) {
        return RandomStringUtils.random(size, false, true);
    }
}
