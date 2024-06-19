package dev.hbm.qris_testingtools.Core.Function.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfig;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static dev.hbm.qris_testingtools.AppConfiguration.Utility.JSONSchemeHelper.getFieldOrDefault;

@Slf4j
@Service("GenerateJWT")
@RequiredArgsConstructor
public class GenerateJWT implements FunctionProcessor {
    protected final long EXPIRATION_TIME = 452_000_000;
    protected final SchemeSecurityConfigService schemeSecurityConfigService;

    @Override
    public Object run(ObjectNode args) {
        String key = getFieldOrDefault(args, "key", "");
        SchemeSecurityConfig st = schemeSecurityConfigService.findById(Long.parseLong(key));

        String aud = getFieldOrDefault(args, "aud", "");
        String iss = getFieldOrDefault(args, "iss", "");
        long exp = getFieldOrDefault(args, "exp", 0L);

        args.remove(List.of("key", "aud", "iss", "exp"));

        JWTCreator.Builder jwt = JWT.create();

        jwt.withAudience(aud)
                .withIssuer(iss)
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + exp));

        for (Iterator<Map.Entry<String, JsonNode>> it = args.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key1 = entry.getKey();
            JsonNode value = entry.getValue();

            jwt.withClaim(key1, value.asText(""));
        }

        String token = "";

        try {
            token = jwt.sign(HMAC512(st.getValue().getBytes()));
        } catch (Exception e) {
            log.error("", e);
        }

        return token;
    }
}
