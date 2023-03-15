package by.piskunou.solvdlaba.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

public interface EmailService {

    Mono<Void> sendMessage(String email, Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException;

}