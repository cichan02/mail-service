package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.SendEmailEvent;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface EmailService {

    Mono<Void> sendMessage(SendEmailEvent sendEmailEvent) throws IOException, TemplateException, MessagingException;

}