package by.piskunou.solvdlaba.config;

import by.piskunou.solvdlaba.domain.event.SendEmailEvent;
import by.piskunou.solvdlaba.kafka.TextXpath;
import com.jcabi.xml.XML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private final XML xml;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, new TextXpath(xml, "//groupId").toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, new TextXpath(xml, "//keyDeserializer").toString());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, new TextXpath(xml, "//valueDeserializer").toString());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, new TextXpath(xml, "//maxPartitionFetchBytesConfig").toString());
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, new TextXpath(xml, "//fetchMaxBytesConfig").toString());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, new TextXpath(xml, "//trustedPackages").toString());
        return props;
    }

    @Bean
    public ReceiverOptions<String, SendEmailEvent> receiverOptions() {
        ReceiverOptions<String, SendEmailEvent> receiverOptions = ReceiverOptions.create(consumerConfig());
        return receiverOptions.subscription(Collections.singleton("sendEmail"))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));
    }

    @Bean
    public KafkaReceiver<String, SendEmailEvent> kafkaReceiver() {
        return KafkaReceiver.create(receiverOptions());
    }

}