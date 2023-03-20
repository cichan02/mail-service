package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.event.SendEmailEvent;
import by.piskunou.solvdlaba.service.EmailService;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final JavaMailSender emailSender;

    @Value("${eureka.client.service-host}")
    private String host;

    @Override
    public Mono<Void> sendMessage(SendEmailEvent sendEmailEvent) {
        try {
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("mail.ftl");
            Map<String, String> templateModel = new HashMap<>();
            templateModel.put("username", sendEmailEvent.getUsername());
            templateModel.put("token", sendEmailEvent.getToken());
            templateModel.put("host", host);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            return sendHtmlMessage(sendEmailEvent.getEmail(), htmlBody);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private Mono<Void> sendHtmlMessage(String to, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Airport");
        helper.setTo(to);
        helper.setSubject("Reset password instructions");
        helper.setText(htmlBody, true);
        emailSender.send(message);
        return Mono.empty();
    }

}
