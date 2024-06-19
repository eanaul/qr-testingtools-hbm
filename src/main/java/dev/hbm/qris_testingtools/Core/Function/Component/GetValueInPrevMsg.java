package dev.hbm.qris_testingtools.Core.Function.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("GetValueInPrevMsg")
public class GetValueInPrevMsg implements FunctionProcessor {
    @Override
    public Object run(ObjectNode args) {
        String fName = args.get("name").asText("");
        String fValue = args.get("value").asText("");

        log.info("GetValueInSameMsg");
        return "GetValueInSameMsg";
    }
}
