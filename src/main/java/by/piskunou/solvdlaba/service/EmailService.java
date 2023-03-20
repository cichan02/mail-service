package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.event.SendEmailEvent;
import reactor.core.publisher.Mono;

public interface EmailService {

    Mono<Void> sendMessage(SendEmailEvent sendEmailEvent);

}