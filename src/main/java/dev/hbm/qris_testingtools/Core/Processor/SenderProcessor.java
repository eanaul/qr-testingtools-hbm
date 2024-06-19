package dev.hbm.qris_testingtools.Core.Processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.Core.Function.Handler.FunctionHandler;
import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig.MessageBodyConfig;
import dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig.MessageBodyConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig.MessageHeaderConfig;
import dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig.MessageHeaderConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageType;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageTypeService;
import dev.hbm.qris_testingtools.SpringLogic.NetworkConfig.NetworkConfigService;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class SenderProcessor extends MessageProcessor {
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    public SenderProcessor(FunctionHandler functionHandler, SchemeConfigService schemeConfigService, NetworkConfigService networkConfigService, MessageTypeService messageTypeService, MessageHeaderConfigService messageHeaderConfigService, MessageBodyConfigService messageBodyConfigService, KafkaTemplate<String, JsonNode> kafkaTemplate) {
        super(functionHandler, schemeConfigService, networkConfigService, messageTypeService, messageHeaderConfigService, messageBodyConfigService);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    protected void logTransaction(ObjectNode containerData, String title, int state) {
        String kafkaKey = UUID.randomUUID().toString();

        ObjectNode node = om.createObjectNode();
        node.put("TITLE", title);
        node.put("STATE", state);
        node.set("TRANS_DATA", containerData);

        kafkaTemplate.send("exe-log-topic", kafkaKey, node);
        containerData.remove(List.of("TITLE", "STATE"));
    }

    public void requestHandler(MessageType messageType, ObjectNode containerData, ObjectNode currentNode) throws JsonProcessingException {
        ObjectNode reqNode = (ObjectNode) currentNode.get("Request");

        ObjectNode scenarioData = getFieldOrDefault(containerData, "SCENARIO_DATA", om.createObjectNode());
        ObjectNode previousScenario = getFieldOrDefault(containerData, "PREVIOUS_SCENARIO", om.createObjectNode());

        //Request Header Config - done
        List<MessageHeaderConfig> reqMessageHeaderConfigs = fetchAllHeaderConfigs(messageType.getId(), MessageStateEnum.REQUEST);

        //Request Body Config - done
        Set<MessageBodyConfig> reqMessageBodyConfigs = fetchNestedBodyConfigs(messageType.getId(), MessageStateEnum.REQUEST);

        log.info("Generate request data");
//        log.info(
//                String.format(
//                        "Got scenario data: \n%s",
//                        containerData.isNull() ? "{ }" : containerData.toPrettyString()
//                )
//        );

        //Generate Query Param
        log.info("Start generate query param");
        generateQueryParam(currentNode, scenarioData);
        log.info("Finish generate query param");

        //Generate Request Body
        log.info("Start generate request message");
        generateMessage(reqMessageBodyConfigs, currentNode, previousScenario, scenarioData, MessageStateEnum.REQUEST, NetworkStateEnum.CLIENT);
        log.info("Finish generate request message");

        //Generate Request Header
        log.info("Start generate request header");
        generateHeader(reqMessageHeaderConfigs, currentNode, previousScenario, scenarioData, MessageStateEnum.REQUEST, NetworkStateEnum.CLIENT);
        log.info("Finish generate request header");

        log.info(
                String.format(
                        "Query param: \n%s",
                        reqNode.get("QueryParam").toPrettyString()
                )
        );

        log.info(
                String.format(
                        "Request Header: \n%s",
                        reqNode.get("HttpHeader").toPrettyString()
                )
        );

        log.info(
                String.format(
                        "Request Body: \n%s",
                        reqNode.get("Body").toPrettyString()
                )
        );

        containerData.set("REQ_DATA", reqNode);

        //Log container data - done
        logTransaction(containerData, "Request Message", 1);
    }

    public void responseHandler(ResponseEntity<ObjectNode> responseEntity, MessageType messageType, ObjectNode containerData, ObjectNode currentNode) throws JsonProcessingException {
        ObjectNode resNode = (ObjectNode) currentNode.get("Response");
        resNode.set("HttpHeader", getHeaderFromHttp(responseEntity));
        resNode.set("Body", responseEntity.getBody());

        log.info(
                String.format(
                        "Response Header: \n%s",
                        resNode.get("HttpHeader").toPrettyString()
                )
        );

        log.info(
                String.format(
                        "Response Body: \n%s",
                        resNode.get("Body").toPrettyString()
                )
        );

        //Response Header Config
        List<MessageHeaderConfig> headerConfigs = fetchAllHeaderConfigs(messageType.getId(), MessageStateEnum.RESPONSE);
        List<String> mandatoryHeader = fetchMandatoryHeader(messageType.getId(), MessageStateEnum.RESPONSE);

        //Response Body Config
        List<MessageBodyConfig> bodyConfigs = fetchAllBodyConfigs(messageType.getId(), MessageStateEnum.RESPONSE);
        List<MessageBodyConfig> mandatoryBody = fetchMandatoryBody(messageType.getId(), MessageStateEnum.RESPONSE);

        ObjectNode fieldNotes = validateField(mandatoryHeader, mandatoryBody, headerConfigs, bodyConfigs, currentNode, NetworkStateEnum.CLIENT);

        containerData.set("RESP_DATA", resNode);
        containerData.setAll(fieldNotes);

        log.info("DATA:  <ALL DATA ARE SET OFF>");
        logContainerData(containerData);

        //Log container data - done
        logTransaction(containerData, "Response Message", 2);
    }

    public void errorHandler(ObjectNode containerData, Throwable e) {
        long scenarioId = getFieldOrDefault(containerData, "SCENARIO_ID", 0L);
        String spanId = getFieldOrDefault(containerData, "SPAN_ID", "");

        ObjectNode temp = om.createObjectNode();
        temp.put("MESSAGE", e.getMessage());
        temp.put("SCENARIO_ID", scenarioId);
        temp.put("SPAN_ID", spanId);

        log.info("", e);

        logTransaction(containerData, "Error message", 4);
    }
}
