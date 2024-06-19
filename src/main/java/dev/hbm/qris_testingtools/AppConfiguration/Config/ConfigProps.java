package dev.hbm.qris_testingtools.AppConfiguration.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProps {
    @Value("${token.secret}")
    public String tokenSecret;

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapAddress;
}
