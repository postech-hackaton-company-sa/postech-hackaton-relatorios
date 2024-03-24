package com.postechhackaton.relatorios.infra.gateway;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmailGatewayImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailGatewayImpl emailGateway;

    @Test
    void enviar_DeveEnviarEmailCorretamente() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        String destinatario = "destinatario@example.com";
        String mensagem = "Olá, este é um teste de e-mail!";

        emailGateway.enviar(destinatario, mensagem);

        verify(emailSender).send(mimeMessage);
    }

}

