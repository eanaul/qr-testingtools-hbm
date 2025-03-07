package dev.hbm.qris_testingtools.AppConfiguration.Kafka;

import com.fasterxml.jackson.databind.JsonNode;
import dev.hbm.qris_testingtools.AppConfiguration.Config.ConfigProps;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    protected final ConfigProps configProps;

    @Bean
    public ProducerFactory<String, JsonNode> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                configProps.bootstrapAddress
        );
        configs.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        configs.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, JsonNode> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
