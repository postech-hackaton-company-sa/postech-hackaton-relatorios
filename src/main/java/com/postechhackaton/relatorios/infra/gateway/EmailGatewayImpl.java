package com.postechhackaton.relatorios.infra.gateway;

import com.postechhackaton.relatorios.domain.gateways.EmailGateway;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailGatewayImpl implements EmailGateway {
    private final JavaMailSender emailSender;

    public EmailGatewayImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void enviar(String destinatario, String mensagem) {
        log.info("Enviando email");
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinatario);
            helper.setSubject("Relatório de funcionário");
            helper.setText(mensagem, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}
