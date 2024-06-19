package dev.hbm.qris_testingtools.Core.Function.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.AppConfiguration.Utility.ApplicationContext;
import dev.hbm.qris_testingtools.AppConfiguration.Utility.JSONSchemeHelper;
import dev.hbm.qris_testingtools.Core.Function.Component.FunctionProcessor;
import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import dev.hbm.qris_testingtools.SpringLogic.EmbedClass.ArgsContainer;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfig;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FunctionHandler extends JSONSchemeHelper {
    private final ObjectMapper om = new ObjectMapper();
    private final SchemeSecurityConfigService schemeSecurityConfigService;

    public Object run(String expression, Collection<ArgsContainer> args, ObjectNode currentNode, ObjectNode prevNode) throws JsonProcessingException {
        ExpressionParser ep = new SpelExpressionParser();
        StandardEvaluationContext sec = new StandardEvaluationContext();
        ObjectNode argsContainer = om.createObjectNode();

        processArgsValue(argsContainer, args, currentNode, prevNode);

        sec.setVariable("securityFunction", getActionProcessorByName(expression));
        sec.setVariable("args", argsContainer);

        Expression ex = ep.parseExpression("#securityFunction.run(#args)");

        return ex.getValue(sec, Object.class);
    }

    private FunctionProcessor getActionProcessorByName(String name) {
        return ApplicationContext.getBean(name, FunctionProcessor.class);
    }

    protected void processArgsValue(ObjectNode argsContainer, Collection<ArgsContainer> argsValues, ObjectNode currentNode, ObjectNode prevNode) throws JsonProcessingException {
        for (ArgsContainer argsValue : argsValues) {
            JsonNode temp;

            switch (argsValue.getSrc()) {
                case CURRENT_QUERY_PARAM:
                    temp = currentNode.at("/Request/QueryParam");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case CURRENT_REQUEST_HEADER:
                    temp = currentNode.at("/Request/HttpHeader");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case CURRENT_REQUEST_BODY:
                    temp = currentNode.at("/Request/Body");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case CURRENT_RESPONSE_HEADER:
                    temp = currentNode.at("/Response/HttpHeader");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));
                    break;

                case CURRENT_RESPONSE_BODY:
                    temp = currentNode.at("/Response/Body");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case PREVIOUS_QUERY_PARAM:
                    temp = prevNode.at("/Request/QueryParam");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case PREVIOUS_REQUEST_HEADER:
                    temp = prevNode.at("/Request/HttpHeader");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case PREVIOUS_REQUEST_BODY:
                    temp = prevNode.at("/Request/Body");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case PREVIOUS_RESPONSE_HEADER:
                    temp = prevNode.at("/Response/HttpHeader");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case PREVIOUS_RESPONSE_BODY:
                    temp = prevNode.at("/Response/Body");

                    if (temp == null) continue;
                    if (temp.isNull()) continue;

                    argsContainer.put(argsValue.getName(), getValueByPath(temp, argsValue.getValue()));

                    break;

                case SECURITY_CONFIG:
                    SchemeSecurityConfig securityConfig = schemeSecurityConfigService.findById(Long.parseLong(argsValue.getValue()));
                    argsContainer.put(argsValue.getName(), securityConfig.getValue());

                    break;

                case INPUT:
                    argsContainer.put(argsValue.getName(), argsValue.getValue());

                    break;

                case INT_FUNCTION:
                    argsContainer.put(argsValue.getName(), run(argsValue.getValue(), argsValue.getChild(), currentNode, prevNode).toString());

                    break;

                case ADDT_DATA:
                    JsonNode addtData = currentNode.at("/Additional");

                    if (addtData == null) continue;
                    if (addtData.isNull()) continue;

                    String vAddtData = getValueByPath(addtData, argsValue.getValue());
                    argsContainer.put(argsValue.getName(), vAddtData);

                    break;

                default:
                    if (argsValue.getType() == FunctionTypeEnum.LIST_DATA) {
                        processArgsValue(argsContainer, argsValue.getChild(), currentNode, prevNode);
                    }

                    break;
            }
        }
    }
}
