package dev.hbm.qris_testingtools.Core.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.Core.Client.HTTP.ClientHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/iso8583/handler/client")
@RequiredArgsConstructor
public class ClientController {
    protected final Tracer tracer;
    protected final ClientHandler clientHandler;

    @PostMapping
    public ObjectNode client(@RequestParam String service, @RequestBody ObjectNode containerData) {
        log.info(
                String.format(
                        "Incoming message from service: [%s] span id: [%s]",
                        service, tracer.currentSpan().context().spanId()
                )
        );

        ObjectNode result = new ObjectMapper().createObjectNode();

        try {
            result = clientHandler.handle(containerData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info(
                    String.format(
                            "Sending response to service: [%s] span id: [%s]",
                            service, tracer.currentSpan().context().spanId()
                    )
            );
        }

        return result;
    }
}
