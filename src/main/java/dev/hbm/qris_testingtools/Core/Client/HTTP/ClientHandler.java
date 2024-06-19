package dev.hbm.qris_testingtools.Core.Client.HTTP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.Core.Function.Handler.FunctionHandler;
import dev.hbm.qris_testingtools.Core.Processor.SenderProcessor;
import dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig.MessageBodyConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig.MessageHeaderConfigService;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageType;
import dev.hbm.qris_testingtools.SpringLogic.MessageType.MessageTypeService;
import dev.hbm.qris_testingtools.SpringLogic.NetworkConfig.NetworkConfig;
import dev.hbm.qris_testingtools.SpringLogic.NetworkConfig.NetworkConfigService;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ClientHandler extends SenderProcessor {

    public ClientHandler(FunctionHandler functionHandler, SchemeConfigService schemeConfigService, NetworkConfigService networkConfigService, MessageTypeService messageTypeService, MessageHeaderConfigService messageHeaderConfigService, MessageBodyConfigService messageBodyConfigService, KafkaTemplate<String, JsonNode> kafkaTemplate) {
        super(functionHandler, schemeConfigService, networkConfigService, messageTypeService, messageHeaderConfigService, messageBodyConfigService, kafkaTemplate);
    }

    public ObjectNode handle(ObjectNode containerData) throws JsonProcessingException {
        log.info("DATA:  <ALL DATA ARE SET OFF>");
        logContainerData(containerData);

        //Get Data
        long schemeId = getFieldOrDefault(containerData, "SCHEME_ID", 0L);
        long networkId = getFieldOrDefault(containerData, "NETWORK_ID", 0L);
        long messageTypeId = getFieldOrDefault(containerData, "MESSAGE_TYPE_ID", 0L);

        //Get Config Information - done
        SchemeConfig schemeConfig = fetchSchemeConfig(schemeId);
        NetworkConfig networkConfig = fetchNetworkConfig(networkId);
        MessageType messageType = fetchMessageType(messageTypeId);

        String dstAddr = networkConfig.getRemoteAddr() + ":" + networkConfig.getRemotePort();
        String uri = messageType.getValue();

        containerData.put("SCHEME_NAME", schemeConfig.getName());
        containerData.put("DST_ADDR", dstAddr);
        containerData.put("URI", uri);
        containerData.put("MESSAGE_TYPE", messageType.getName());

        ObjectNode addtNode = om.createObjectNode();
        ObjectNode netConfigAddtValue = builderAddtContainer(networkConfig.getAddtValues());
        ObjectNode msgTypeAddtValue = builderAddtContainer(messageType.getAddtValues());

        addtNode.setAll(netConfigAddtValue);
        addtNode.setAll(msgTypeAddtValue);

        ObjectNode currentNode = om.createObjectNode();
        currentNode.set("Additional", addtNode);
        currentNode.set("Request", buildContainer());
        currentNode.set("Response", buildContainer());

        requestHandler(messageType, containerData, currentNode);

        log.info(String.format("Sending request to Address: [%s] Port: [%s] Uri: [%s]", networkConfig.getRemoteAddr(), networkConfig.getRemotePort(), uri));

        try {
            CompletableFuture<ResponseEntity<ObjectNode>> responseFuture = new CompletableFuture<>();
            CompletableFuture<ResponseEntity<ObjectNode>> timeoutFuture = responseFuture
                    .orTimeout(messageType.getToTime(), TimeUnit.SECONDS); // Set the timeout to 30 seconds

            WebClient.builder()
                    .baseUrl("http://" + dstAddr)
                    .build()
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(uri)
                            .build()
                    )
                    .headers(httpHeaders -> httpHeaders.addAll(convertNodeToHeader((ObjectNode) currentNode.at("/Request/HttpHeader"))))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(currentNode.at("/Request/Body"))
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> {
                        // Handle error responses here
                        log.error("Received error response: {}", clientResponse.statusCode());
                        return Mono.error(new Exception("Received error response: " + clientResponse.statusCode()));
                    })
                    .toEntity(ObjectNode.class)
                    .subscribeOn(Schedulers.single())
                    .flatMap(response -> {
                        try {
                            if (timeoutFuture.isCompletedExceptionally())
                                log.info(
                                        String.format(
                                                "Receive late response from Address: [%s] Port: [%s] Uri: [%s]",
                                                networkConfig.getRemoteAddr(), networkConfig.getRemotePort(), uri
                                        )
                                );
                            else
                                log.info(
                                        String.format(
                                                "Receive response from Address: [%s] Port: [%s] Uri: [%s]",
                                                networkConfig.getRemoteAddr(), networkConfig.getRemotePort(), uri
                                        )
                                );

                            responseHandler(response, messageType, containerData, currentNode);
                        } catch (Exception e) {
                            errorHandler(containerData, e);
                        }

                        return Mono.just(response);
                    })
                    .toFuture()
                    .exceptionally(throwable -> {
                        errorHandler(containerData, throwable);
                        return null;
                    })
                    .whenComplete((response, throwable) -> {
                        responseFuture.complete(response);
                    });

            timeoutFuture.join();
        } catch (CompletionException e) {
            log.info(
                    String.format(
                            "Request timeout from Address: [%s] Port: [%s] Uri: [%s]",
                            networkConfig.getRemoteAddr(), networkConfig.getRemotePort(), uri
                    )
            );

            logTransaction(containerData, "Request timeout", 3);
        }

        return containerData;
    }
}
