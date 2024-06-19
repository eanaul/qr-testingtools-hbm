package dev.hbm.qris_testingtools.AppConfiguration.Utility;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.hbm.qris_testingtools.AppConfiguration.Utility.LoggerHelper.rightPaddingString;

@Slf4j
public class JSONSchemeHelper {
    protected final String pathIndexPattern = "\\[(.*?)\\]";

    protected void decimalJsonMapper(ObjectMapper om) {
        om.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        om.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    }

    protected boolean isArrayPath(String path) {
        Pattern pattern = Pattern.compile(pathIndexPattern);
        return pattern.matcher(path).find();
    }

    protected int getArrayIndex(String path) {
        Pattern pattern = Pattern.compile(pathIndexPattern);
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException("Index not found");
        }
    }

    protected String clearArrayPath(String path) {
        return path.replaceAll(pathIndexPattern, "");
    }

    protected String getValueByPath(JsonNode node, String path) {
        String value = "";
        String[] pathComponents = path.split("\\.");

        for (String pathComponent : pathComponents) {
            if (isArrayPath(pathComponent)) {
                String clrPath = pathComponent.replaceAll(pathIndexPattern, "");
                int index = getArrayIndex(pathComponent);

                if (!node.has(clrPath)) {
                    break;
                }

                ArrayNode arrayNode = (ArrayNode) node.get(clrPath);
                JsonNode valueNode = arrayNode.get(index);

                if (valueNode.isNull()) {
                    break;
                } else if (valueNode.isValueNode() && !valueNode.isObject()) {
                    value = valueNode.asText("");
                    break;
                } else {
                    node = arrayNode.get(index);
                }
            } else {
                if (!node.has(pathComponent)) {
                    break;
                } else if (node.get(pathComponent).isValueNode() && !node.get(pathComponent).isObject()){
                    value = node.get(pathComponent).asText("");
                } else {
                    node = node.get(pathComponent);
                }
            }
        }

        return value;
    }

    public static <T> T getFieldOrDefault(JsonNode node, String fieldName, T defaultValue) {
        JsonNode fieldNode = node.at("/" + fieldName);
        if (fieldNode != null && !fieldNode.isNull()) {
            if (defaultValue instanceof String) {
                return (T) fieldNode.asText("");
            } else if (defaultValue instanceof Integer) {
                return (T) (Integer) fieldNode.asInt(0);
            } else if (defaultValue instanceof Long) {
                return (T) (Long) fieldNode.asLong(0);
            } else if (defaultValue instanceof Boolean) {
                return (T) (Boolean) fieldNode.asBoolean(false);
            } else if (defaultValue instanceof ObjectNode) {
                if (fieldNode instanceof MissingNode) {
                    return defaultValue;
                }
                return (T) fieldNode;
            } else if (defaultValue instanceof ArrayNode) {
                return (T) fieldNode;
            }
            else {
                throw new IllegalArgumentException("Unsupported type: " + defaultValue.getClass());
            }
        } else {
            return defaultValue;
        }
    }

    protected void logContainerData(JsonNode containerData) {
        if (containerData.isObject()) {
            for (Iterator<Map.Entry<String, JsonNode>> it = containerData.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                if (fieldValue.isArray() || fieldValue.isObject())
                    log.info(rightPaddingString(fieldName, 20) + ": " + fieldValue);
                else
                    log.info(rightPaddingString(fieldName, 20) + ": " + fieldValue.asText(""));
            }
        }
    }
}
