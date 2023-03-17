package by.piskunou.solvdlaba.kafka;

import by.piskunou.solvdlaba.domain.SendEmailEvent;
import by.piskunou.solvdlaba.service.EmailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
                    try {
                        emailService.sendMessage(record.value()).subscribe();
                    } catch (IOException | TemplateException | MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    record.receiverOffset().acknowledge();
                });
    }

}

