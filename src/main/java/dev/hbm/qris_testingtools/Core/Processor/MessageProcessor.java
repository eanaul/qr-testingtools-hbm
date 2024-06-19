package dev.hbm.qris_testingtools.Core.Processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dev.hbm.qris_testingtools.AppConfiguration.Utility.JSONSchemeHelper;
import dev.hbm.qris_testingtools.Core.Function.Handler.FunctionHandler;
import dev.hbm.qris_testingtools.Enum.DataTypeEnum;
import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.AddtValue;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.ArgsContainer;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.ConfigCondition;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.FunctionArgsValue;
import dev.hbm.qris_testingtools.SpringLogic.FieldConfig.FieldConfig;
import dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig.MessageBodyConfig;
import dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig.MessageBodyConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig.MessageHeaderConfig;
import dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig.MessageHeaderConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageType;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageTypeService;
import dev.hbm.qris_testingtools.SpringLogic.NetworkConfig.NetworkConfig;
import dev.hbm.qris_testingtools.SpringLogic.NetworkConfig.NetworkConfigService;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class MessageProcessor extends JSONSchemeHelper {
    protected final ObjectMapper om = new ObjectMapper();
    protected final FunctionHandler functionHandler;
    protected final SchemeConfigService schemeConfigService;
    protected final NetworkConfigService networkConfigService;
    protected final MessageTypeService messageTypeService;
    protected final MessageHeaderConfigService messageHeaderConfigService;
    protected final MessageBodyConfigService messageBodyConfigService;

    public MessageProcessor(FunctionHandler functionHandler, SchemeConfigService schemeConfigService, NetworkConfigService networkConfigService, MessageTypeService messageTypeService, MessageHeaderConfigService messageHeaderConfigService, MessageBodyConfigService messageBodyConfigService) {
        decimalJsonMapper(om);
        this.functionHandler = functionHandler;
        this.schemeConfigService = schemeConfigService;
        this.networkConfigService = networkConfigService;
        this.messageTypeService = messageTypeService;
        this.messageHeaderConfigService = messageHeaderConfigService;
        this.messageBodyConfigService = messageBodyConfigService;
    }

    protected SchemeConfig fetchSchemeConfig(long id) {
        return schemeConfigService.findById(id);
    }

    protected NetworkConfig fetchNetworkConfig(long id) {
        return networkConfigService.findById(id);
    }

    protected NetworkConfig fetchNetworkConfig(String ip, int port) {
        return networkConfigService.fetchLocalConnection(ip, port);
    }

    protected MessageType fetchMessageType(long id) {
        return messageTypeService.findById(id);
    }

    protected MessageType fetchMessageType(long schemeId, String value) {
        return messageTypeService.findBySchemeIdAndValue(schemeId, value);
    }

    protected List<MessageHeaderConfig> fetchAllHeaderConfigs(long typeId, MessageStateEnum state) {
        return messageHeaderConfigService.findAllByMessageTypeAndState(typeId, state);
    }

    protected List<String> fetchMandatoryHeader(long typeId, MessageStateEnum state) {
        return messageHeaderConfigService.findAllMandatoryFieldByTypeAndState(typeId, state);
    }

    protected List<MessageBodyConfig> fetchAllBodyConfigs(long typeId, MessageStateEnum state) {
        return messageBodyConfigService.findAllByMessageTypeAndState(typeId, state);
    }

    protected List<MessageBodyConfig> fetchMandatoryBody(long typeId, MessageStateEnum state) {
        return messageBodyConfigService.findAllMandatoryFieldByTypeAndState(typeId, state);
    }

    protected Set<MessageBodyConfig> fetchNestedBodyConfigs(long typeId, MessageStateEnum state) {
        return messageBodyConfigService.findAllNestedChild(typeId, state);
    }

    protected boolean isRequestPostMethod(HttpExchange exchange) {
        return "POST".equals(exchange.getRequestMethod());
    }

    protected ObjectNode builderAddtContainer(Collection<AddtValue> addtValues) {
        ObjectNode temp = om.createObjectNode();

        for (AddtValue addtValue : addtValues) {
            temp.put(addtValue.getName(), addtValue.getValue());
        }

        return temp;
    }

    protected ObjectNode buildContainer() {
        ObjectNode temp = om.createObjectNode();
        temp.set("QueryParam", om.createObjectNode());
        temp.set("HttpHeader", om.createObjectNode());
        temp.set("Body", om.createObjectNode());
        return temp;
    }

    protected JsonNode buildMessage(Map<String, Object> rawData) {
        return this.om.valueToTree(rawData);
    }

    protected JsonNode getQueryParamFromHttp(HttpExchange exchange) {
        ObjectNode temp = om.createObjectNode();
        String query = exchange.getRequestURI().getRawQuery();

        if (query == null) {
            return temp;
        }

        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                temp.put(entry[0], entry[1]);
            } else {
                temp.put(entry[0], "");
            }
        }
        return temp;
    }

    protected JsonNode getHeaderFromHttp(HttpExchange exchange) {
        ObjectNode tempNode = om.createObjectNode();
        Headers headers = exchange.getRequestHeaders();

        for (String hName : headers.keySet()) {
            StringBuilder tempValue = new StringBuilder();

            for (String hValue : headers.get(hName)) {
                tempValue.append(hValue);
            }

            tempNode.put(hName, tempValue.toString());
        }

        return tempNode;
    }

    protected JsonNode getHeaderFromHttp(ResponseEntity<?> response) {
        ObjectNode tempNode = om.createObjectNode();

        for (String hName : response.getHeaders().keySet()) {
            StringBuilder tempValue = new StringBuilder();

            for (String hValue : Objects.requireNonNull(response.getHeaders().get(hName))) {
                tempValue.append(hValue);
            }

            tempNode.put(hName, tempValue.toString());
        }

        return tempNode;
    }

    protected JsonNode getBodyFromHttp(HttpExchange exchange) throws IOException {
        return om.readTree(exchange.getRequestBody());
    }

    protected ObjectNode validateField(Collection<String> mandatoryHeader, Collection<MessageBodyConfig> mandatoryBody, Collection<MessageHeaderConfig> headerConfigs, List<MessageBodyConfig> bodyConfigs, ObjectNode currentNode, NetworkStateEnum networkState) throws JsonProcessingException {
        ObjectNode mNode = (ObjectNode) (networkState == NetworkStateEnum.CLIENT ? currentNode.get("Response") : currentNode.get("Request"));
        ObjectNode mHeader = (ObjectNode) mNode.get("HttpHeader");
        ObjectNode mBody = (ObjectNode) mNode.get("Body");
        ObjectNode temp = om.createObjectNode();

        //Validate Mandatory Header
        log.info("Checking mandatory header");
        ArrayNode notFoundHeader = om.createArrayNode();
        validateMandatoryHeader(mandatoryHeader, mHeader, notFoundHeader);
        log.info("Finish checking mandatory header");

        //Validate un-required and value type header
        log.info("Checking un-required, value type and function header");
        ArrayNode unRequiredHeader = om.createArrayNode();
        ArrayNode inCorrectValueTypeHeader = om.createArrayNode();
        ArrayNode notValidAuth = om.createArrayNode();
        validateUnRequiredFieldAndValueType(mHeader, currentNode, "", headerConfigs, unRequiredHeader, inCorrectValueTypeHeader, notValidAuth, networkState);
        log.info("Finish checking un-required, value type and function header");

        //Validate Mandatory Body
        log.info("Checking mandatory body");
        ArrayNode notFoundBody = om.createArrayNode();
        validateMandatoryBody(mandatoryBody, mBody, notFoundBody, "");
        log.info("Finish checking mandatory body");

        //Validate un-required and value type body
        log.info("Checking un-required and value type body");
        ArrayNode unRequiredBody = om.createArrayNode();
        ArrayNode inCorrectValueTypeBody = om.createArrayNode();
        validateUnRequiredFieldAndValueType(mBody, "", bodyConfigs, unRequiredBody, inCorrectValueTypeBody);
        log.info("Finish checking un-required and value type body");

        //Header note
        temp.set("NFH", notFoundHeader);
        temp.set("URH", unRequiredHeader);
        temp.set("ITH", inCorrectValueTypeHeader);

        temp.set("NVA", notValidAuth);

        //Body note
        temp.set("NFB", notFoundBody);
        temp.set("URB", unRequiredBody);
        temp.set("ITB", inCorrectValueTypeBody);

        return temp;
    }

    private void validateMandatoryHeader(Collection<String> mandatoryFields, JsonNode node, ArrayNode note) {
        for (String mandatoryField : mandatoryFields) {
            String lowerCased = mandatoryField.toLowerCase();
            String cvtName = lowerCased.substring(0, 1).toUpperCase() + lowerCased.substring(1);
            JsonNode v1 = node.get(cvtName);

            if (v1 == null) {
                log.info(
                        String.format(
                                "Field: [%s] not found in request",
                                cvtName
                        )
                );

                note.add(cvtName);
            }
        }
    }

    private void validateMandatoryBody(Collection<MessageBodyConfig> mandatoryFields, JsonNode node, ArrayNode note, String parentField) {
        for (MessageBodyConfig mandatoryField : mandatoryFields) {
            String fName = mandatoryField.getFieldConfig().getFieldName();
            JsonNode v1 = node.get(fName);

            String currentField = parentField.isEmpty() ? fName : parentField + "." + fName;

            if (v1 == null) {
                log.info(
                        String.format(
                                "Field: [%s] not found in request",
                                currentField
                        )
                );

                note.add(currentField);
            } else {
                if (mandatoryField.getChildConfig().size() > 0) {
                    if (v1.isArray()) {
                        for (int i = 0; i < v1.size(); i++) {
                            validateMandatoryBody(mandatoryField.getChildConfig(), v1.get(i), note, currentField + "[" + i + "]");
                        }
                    } else {
                        validateMandatoryBody(mandatoryField.getChildConfig(), v1, note, currentField);
                    }
                }
            }
        }
    }

    private void validateUnRequiredFieldAndValueType(JsonNode node, ObjectNode currentNode, String parentField, Collection<MessageHeaderConfig> messageHeaderConfigs, ArrayNode unRequiredField, ArrayNode inCorrectType, ArrayNode notValidAuth, NetworkStateEnum networkState) throws JsonProcessingException {
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();

            String currentField = parentField.isEmpty() ? fieldName : parentField + "." + fieldName;

            List<MessageHeaderConfig> filteredMessageHeaderConfig = messageHeaderConfigs.stream()
                    .filter(v1 -> Objects.equals(v1.getHeaderConfig().getFieldName(), fieldName))
                    .collect(Collectors.toList());

            if (filteredMessageHeaderConfig.isEmpty()) {
                log.info(
                        String.format(
                                "Field: [%s] is un-required",
                                currentField
                        )
                );
                unRequiredField.add(currentField);
            } else {
                MessageHeaderConfig messageHeaderConfig = filteredMessageHeaderConfig.get(0);
                String valueTypeNote = validateValueType(fieldValue, currentField, messageHeaderConfig.getHeaderConfig().getDataType());

                if (!valueTypeNote.isEmpty()) {
                    inCorrectType.add(valueTypeNote);
                } else {
                    if (messageHeaderConfig.isUseFunction()) {
                        String notValidNote = validateFunctionHeader(messageHeaderConfig, currentNode, networkState);

                        if (!notValidNote.isEmpty()) {
                            notValidAuth.add(notValidNote);
                        }
                    }
                }
            }
        }
    }

    private void validateUnRequiredFieldAndValueType(JsonNode node, String parentField, List<MessageBodyConfig> messageBodyConfigs, ArrayNode unRequiredField, ArrayNode inCorrectType) {
        if (node.isObject()) {
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                String currentField = parentField.isEmpty() ? fieldName : parentField + "." + fieldName;

                List<MessageBodyConfig> filteredMessageBodyConfig = messageBodyConfigs.stream()
                        .filter(v1 -> Objects.equals(clearArrayPath(v1.getPath()), clearArrayPath(currentField)))
                        .collect(Collectors.toList());

                if (filteredMessageBodyConfig.isEmpty()) {
                    log.info(
                            String.format(
                                    "Field: [%s] is un-required",
                                    currentField
                            )
                    );
                    unRequiredField.add(currentField);
                } else {
                    MessageBodyConfig messageBodyConfig = filteredMessageBodyConfig.get(0);
                    String valueTypeNote = validateValueType(fieldValue, currentField, messageBodyConfig.getFieldConfig().getDataType());

                    if (!valueTypeNote.isEmpty()) {
                        inCorrectType.add(valueTypeNote);
                    }
                }

                if (fieldValue.isObject() || fieldValue.isArray()) {
                    validateUnRequiredFieldAndValueType(fieldValue, currentField, messageBodyConfigs, unRequiredField, inCorrectType);
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                validateUnRequiredFieldAndValueType(node.get(i), parentField + "[" + i + "]", messageBodyConfigs, unRequiredField, inCorrectType);
            }
        }
    }

    private String validateValueType(JsonNode node, String fieldName, DataTypeEnum dataType) {
        DataTypeEnum tempType = convertDataType(node);

        if (dataType == tempType) {
            return "";
        }

        log.info(
                String.format(
                        "Field: [%s] have incorrect value type",
                        fieldName
                )
        );

        return fieldName;
    }

    private DataTypeEnum convertDataType(JsonNode node) {
        if (node.getNodeType() == JsonNodeType.ARRAY) {
            ArrayNode tempArray = (ArrayNode) node;
            JsonNodeType firstData = tempArray.get(0).getNodeType();

            if (firstData == JsonNodeType.STRING) {
                return DataTypeEnum.ARRAY_STRING;
            } else if (firstData == JsonNodeType.OBJECT) {
                return DataTypeEnum.ARRAY_OBJ;
            } else {
                throw new IllegalArgumentException("Unlisted field type");
            }
        } else if (node.getNodeType() == JsonNodeType.STRING) {
            return DataTypeEnum.STRING;
        } else if (node.getNodeType() == JsonNodeType.NUMBER) {
            return DataTypeEnum.NUMBER;
        } else if (node.getNodeType() == JsonNodeType.OBJECT) {
            return DataTypeEnum.OBJECT;
        } else if (node.getNodeType() == JsonNodeType.BOOLEAN) {
            return DataTypeEnum.BOOLEAN;
        } else if (node instanceof DecimalNode) {
            return DataTypeEnum.AMOUNT;
        } else if (node.getNodeType() == JsonNodeType.NULL) {
            return DataTypeEnum.NULL;
        }

        throw new IllegalArgumentException("Unlisted field type");
    }

    private String validateFunctionHeader(MessageHeaderConfig headerConfig, ObjectNode currentNode, NetworkStateEnum networkState) throws JsonProcessingException {
        String note = "";

        for (FunctionArgsValue function : headerConfig.getFunctions()) {
            if (function.getNetworkState() == networkState) {
                note = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, om.createObjectNode()).toString();
            }
        }

        return note;
    }

    protected void setNodeByScenarioData(JsonNode v1, ObjectNode v2) {
        if (!v1.isNull())
            for (Iterator<Map.Entry<String, JsonNode>> it = v1.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                setMessageValue(v2, fieldName, fieldValue);
            }
    }

    protected void generateQueryParam(ObjectNode currentNode, JsonNode scenarioData) {
        ObjectNode temp = (ObjectNode) currentNode.at("/Request/QueryParam");
        setNodeByScenarioData(scenarioData.get("QueryParam"), temp);
    }

    protected void generateHeader(Collection<MessageHeaderConfig> configs, ObjectNode currentNode, ObjectNode prevNode, JsonNode scenarioData, MessageStateEnum state, NetworkStateEnum networkState) throws JsonProcessingException {
        ObjectNode temp = (ObjectNode) (state == MessageStateEnum.REQUEST ? currentNode.at("/Request/HttpHeader") : currentNode.at("/Response/HttpHeader"));
        setNodeByScenarioData(scenarioData.get("HttpHeader"), temp);
        setHeaderValue(configs, currentNode, prevNode, temp, networkState);
    }

    protected void setHeaderValue(Collection<MessageHeaderConfig> configs, ObjectNode currentNode, ObjectNode prevNode, ObjectNode headerNode, NetworkStateEnum networkState) throws JsonProcessingException {
        for (MessageHeaderConfig messageHeaderConfig : configs) {
            if (messageHeaderConfig.isUseFunction()) {
                for (FunctionArgsValue function : messageHeaderConfig.getFunctions()) {
                    if (function.getNetworkState() == networkState) {
                        Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);

                        if (value instanceof Integer) {
                            headerNode.put(messageHeaderConfig.getHeaderConfig().getFieldName(), (Integer) value);
                        } else if (value instanceof Boolean) {
                            headerNode.put(messageHeaderConfig.getHeaderConfig().getFieldName(), (Boolean) value);
                        } else if (value instanceof BigDecimal) {
                            headerNode.put(messageHeaderConfig.getHeaderConfig().getFieldName(), (BigDecimal) value);
                        } else {
                            if (value != null) {
                                headerNode.put(messageHeaderConfig.getHeaderConfig().getFieldName(), (String) value);
                            }
                        }
                    }
                }

            } else {
                headerNode.put(messageHeaderConfig.getHeaderConfig().getFieldName(), messageHeaderConfig.getDefaultValue());
            }
        }
    }

    protected void generateMessage(Set<MessageBodyConfig> configs, ObjectNode currentNode, ObjectNode prevNode, JsonNode scenarioData, MessageStateEnum state, NetworkStateEnum networkState) throws JsonProcessingException {
        ObjectNode temp = (ObjectNode) (state == MessageStateEnum.REQUEST ? currentNode.at("/Request/Body") : currentNode.at("/Response/Body"));
        setNodeByScenarioData(scenarioData.get("Body"), temp);
        setMessageValue(configs, currentNode, prevNode, temp, networkState);
    }

    protected void setMessageValue(ObjectNode node, String path, JsonNode value) {
        String[] pathComponents = path.split("\\.");
        String lastPath = pathComponents[pathComponents.length - 1].replaceAll(pathIndexPattern, "");

        for (int i = 0; i < pathComponents.length; i++) {
            String pathComponent = pathComponents[i];

            if (isArrayPath(pathComponent)) {
                String clrPath = pathComponent.replaceAll(pathIndexPattern, "");
                int index = getArrayIndex(pathComponent);

                if (!node.has(clrPath)) {
                    node.putArray(clrPath);
                }

                ArrayNode arrayNode = (ArrayNode) node.get(clrPath);

                while (arrayNode.size() <= index) {
                    arrayNode.addNull();
                }

                JsonNode aElement = arrayNode.get(index);

                if (aElement instanceof NullNode) {
                    ObjectNode objectNode = node.objectNode();

                    if (i == pathComponents.length - 1) {
                        arrayNode.set(index, value);
                    } else {
                        arrayNode.set(index, objectNode);
                        node = objectNode;
                    }
                } else {
                    node = (ObjectNode) arrayNode.get(index);
                }
            } else {
                if (!node.has(pathComponent)) {
                    if (i == pathComponents.length - 1) {
                        node.set(lastPath, value);
                    } else {
                        node.putObject(pathComponent);
                        node = (ObjectNode) node.get(pathComponent);
                    }
                } else {
                    node = (ObjectNode) node.get(pathComponent);
                }
            }
        }
    }

    protected void setMessageValue(Set<MessageBodyConfig> messageBodyConfigs, ObjectNode currentNode, ObjectNode prevNode, ObjectNode messageNode, NetworkStateEnum networkState) throws JsonProcessingException {
        for (MessageBodyConfig messageBodyConfig : messageBodyConfigs) {
            //Store the actual node to tempNode, to prevent inconsistent data
            ObjectNode tempNode = messageNode;
            FieldConfig fieldConfig = messageBodyConfig.getFieldConfig();
            String fieldName = fieldConfig.getFieldName();

            if (!messageBodyConfig.getConditions().isEmpty()) {
                boolean conditionStat = evaluateCondition(messageBodyConfig.getConditions(), currentNode.at("/Request/Body"), currentNode.at("/Response/Body"));

                if (!conditionStat) {
                    if (tempNode.has(fieldName)) {
                        tempNode.remove(fieldName);
                    }

                    continue;
                }
            }

            if (!tempNode.has(fieldName)) {
                switch (fieldConfig.getDataType()) {
                    case OBJECT:
                        tempNode.putObject(fieldName);
                        tempNode = (ObjectNode) tempNode.get(fieldName);
                        break;

                    case STRING:
                        if (messageBodyConfig.isUseFunction()) {
                            for (FunctionArgsValue function : messageBodyConfig.getFunctions()) {
                                if (function.getNetworkState() == networkState) {
                                    Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);
                                    tempNode.put(fieldName, (String) value);
                                }
                            }
                        } else {
                            tempNode.put(fieldName, messageBodyConfig.getDefaultValue());
                        }
                        break;

                    case NUMBER:
                        if (messageBodyConfig.isUseFunction()) {
                            for (FunctionArgsValue function : messageBodyConfig.getFunctions()) {
                                if (function.getNetworkState() == networkState) {
                                    Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);
                                    tempNode.put(fieldName, (Integer) value);
                                }
                            }

                        } else {
                            tempNode.put(fieldName, Integer.parseInt(messageBodyConfig.getDefaultValue()));
                        }
                        break;

                    case BOOLEAN:
                        if (messageBodyConfig.isUseFunction()) {
                            for (FunctionArgsValue function : messageBodyConfig.getFunctions()) {
                                if (function.getNetworkState() == networkState) {
                                    Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);
                                    tempNode.put(fieldName, (Boolean) value);
                                }
                            }
                        } else {
                            tempNode.put(fieldName, Boolean.valueOf(messageBodyConfig.getDefaultValue()));
                        }
                        break;

                    case ARRAY_OBJ:
                        ArrayNode arrayObj = om.createArrayNode();
                        arrayObj.add(tempNode.objectNode());
                        tempNode.set(fieldName, arrayObj);
                        tempNode = (ObjectNode) arrayObj.get(0);
                        break;

                    case ARRAY_STRING:
                        ArrayNode arrayString = om.createArrayNode();

                        if (messageBodyConfig.isUseFunction()) {
                            for (FunctionArgsValue function : messageBodyConfig.getFunctions()) {
                                if (function.getNetworkState() == networkState) {
                                    Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);
                                    arrayString.add((String) value);
                                }
                            }
                        } else {
                            arrayString.add(messageBodyConfig.getDefaultValue());
                        }
                        tempNode.set(fieldName, arrayString);
                        break;
                }
            } else {
                switch (fieldConfig.getDataType()) {
                    case OBJECT:
                        tempNode = (ObjectNode) tempNode.get(fieldName);
                        break;

                    case ARRAY_OBJ:
                        ArrayNode arrayObj = (ArrayNode) tempNode.get(fieldName);

                        for (int i = 0; i < arrayObj.size(); i++) {
                            JsonNode childNode = arrayObj.get(i);

                            ObjectNode childObj = childNode.isNull() ? om.createObjectNode() : (ObjectNode) childNode;
                            setMessageValue(messageBodyConfig.getChildConfig(), currentNode, prevNode, childObj, networkState);

                            if (childNode.isNull()) {
                                arrayObj.set(i, childObj);
                            }
                        }
                        continue;

                    case ARRAY_STRING:
                        ArrayNode arrayString = (ArrayNode) tempNode.get(fieldName);

                        for (int i = 0; i < arrayString.size(); i++) {
                            JsonNode childNode = arrayString.get(i);

                            if (childNode.isNull()) {
                                if (messageBodyConfig.isUseFunction()) {
                                    for (FunctionArgsValue function : messageBodyConfig.getFunctions()) {
                                        if (function.getNetworkState() == networkState) {
                                            Object value = functionHandler.run(function.getName(), convertArgs(function.getArgs()), currentNode, prevNode);
                                            arrayString.add((String) value);
                                        }
                                    }
                                } else {
                                    arrayString.set(i, messageBodyConfig.getDefaultValue());
                                }
                            }
                        }
                        continue;
                }
            }

            if (messageBodyConfig.getChildConfig().size() > 0) {
                setMessageValue(messageBodyConfig.getChildConfig(), currentNode, prevNode, tempNode, networkState);
            }
        }
    }

    protected boolean evaluateCondition(Collection<ConfigCondition> conditions, JsonNode reqNode, JsonNode respNode) {
        boolean conditionStat = false;

        for (ConfigCondition condition : conditions) {
            switch (condition.getPath()) {
                case "RequestNode":
                    String v1 = getValueByPath(reqNode, condition.getParam());
                    conditionStat = evaluateConditionValue(condition.getOperator(), condition.getValue(), v1);
                    break;

                case "ResponseNode":
                    String v2 = getValueByPath(respNode, condition.getParam());
                    conditionStat = evaluateConditionValue(condition.getOperator(), condition.getValue(), v2);
                    break;
            }
        }

        return conditionStat;
    }

    protected boolean evaluateConditionValue(String operator, String v1, String v2) {
        switch (operator) {
            case "=":
                return Objects.equals(v1, v2);

            default:
                return false;
        }
    }

    protected HttpHeaders convertNodeToHeader(ObjectNode node) {
        HttpHeaders httpHeaders = new HttpHeaders();
        node.fields().forEachRemaining(entry -> {
            httpHeaders.add(entry.getKey(), entry.getValue().asText());
        });
        return httpHeaders;
    }

    private List<ArgsContainer> convertArgs(String args) throws JsonProcessingException {
        if (args != null) {
            TypeReference<List<ArgsContainer>> typeReference = new TypeReference<>() {
            };
            return om.readValue(args, typeReference);
        } else return new ArrayList<>();
    }

    protected abstract void logTransaction(ObjectNode containerData, String title, int state);
}
