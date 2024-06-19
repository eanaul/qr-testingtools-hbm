package dev.hbm.qris_testingtools.Core.Processor;

import dev.hbm.qris_testingtools.SpringLogic.TagConfig.TagConfig;
import dev.hbm.qris_testingtools.SpringLogic.TagConfig.TagConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QRProcessor {
    @Autowired
    private TagConfigRepo tagConfigRepository;

    public void parseHandler(String message){
        Map<String, String> tlvData = parseTLV(message);
        for (Map.Entry<String, String> entry : tlvData.entrySet()) {
            log.info("Tag: {}, Value: {}", entry.getKey(), entry.getValue());
            List<TagConfig> tagConfigs = tagConfigRepository.findByTagId(Integer.parseInt(entry.getKey()));
            for(TagConfig tagConfig : tagConfigs){

                if(tagConfig.isHasChild()){
                    log.info("Tag: {}, Has a child", entry.getKey());
                    Map<String, String> tlvData2 = parseTLV(entry.getValue());
                    for (Map.Entry<String, String> entryChild : tlvData2.entrySet()) {
                        log.info("Child of {}, Tag: {}, Value: {}", entry.getKey(), entryChild.getKey(), entryChild.getValue());
                    }

                }
            }
        }
    }

    private Map<String, String> parseTLV(String tlvString) {
        Map<String, String> tlvData = new HashMap<>();
        int i = 0;
        while (i < tlvString.length()) {
            try {
                String tag = tlvString.substring(i, i + 2);
                int length = Integer.parseInt(tlvString.substring(i + 2, i + 4));
                String value = tlvString.substring(i + 4, i + 4 + length);
                tlvData.put(tag, value);
                i += 4 + length;
            } catch (Exception e) {
                log.error("Error parsing TLV string", e);
                break;
            }
        }
        return tlvData;
    }
}
