package by.piskunou.solvdlaba.kafka;

import by.piskunou.solvdlaba.domain.event.SendEmailEvent;
import by.piskunou.solvdlaba.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaReceiver<String, SendEmailEvent> kafkaReceiver;
    private final EmailService emailService;

    @PostConstruct
    public void init(){
        fetch();
    }

    public void fetch() {
        kafkaReceiver.receive()
                .doOnError(error -> log.error("Failed to send email", error))
                .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofMinutes(1)))
                .doOnNext(record -> log.debug("Received event: key {}", record.key()))
                .subscribe(record -> {
                    emailService.sendMessage(record.value())
                            .doOnError(e -> log.error("Failed to send email", e.getMessage()))
                            .subscribe();
                    record.receiverOffset().acknowledge();
                });
    }

}

