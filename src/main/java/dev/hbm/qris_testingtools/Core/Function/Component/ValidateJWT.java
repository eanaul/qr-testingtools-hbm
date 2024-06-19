package dev.hbm.qris_testingtools.Core.Function.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.hbm.qris_testingtools.AppConfiguration.Config.ConfigProps;
import dev.hbm.qris_testingtools.AppConfiguration.Utility.JSONSchemeHelper;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfig;
import dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig.SchemeSecurityConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("ValidateJWT")
@RequiredArgsConstructor
public class ValidateJWT extends JSONSchemeHelper implements FunctionProcessor {
    protected final ConfigProps configProps;
    protected final SchemeSecurityConfigService schemeSecurityConfigService;

    @Override
    public Object run(ObjectNode args) {
        String key = getFieldOrDefault(args, "key", "");
        SchemeSecurityConfig st = schemeSecurityConfigService.findById(Long.parseLong(key));

        String token = getFieldOrDefault(args, "token", "");

        if (st.getValue().isEmpty()) {
            return "Function result: [Key not found]";
        }

        if (token.isEmpty()) {
            return "Function result: [Token not found]";
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(st.getValue())).build();
            Date expiration = verifier.verify(token).getExpiresAt();
            boolean isExp = expiration.before(new Date());

            if (isExp) {
                log.info("Function result: [TOKEN ISN'T VALID]");
                return "Function result: [TOKEN ISN'T VALID]";
            } else {
//                log.info("Function result: [TOKEN IS VALID]");
            }
        } catch (Exception e) {
            log.error("", e);
            return "Function result: [" + e.getMessage() + "]";
        }

        return "";
    }
}
