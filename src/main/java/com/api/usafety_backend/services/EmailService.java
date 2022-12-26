package com.api.usafety_backend.services;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.util.Constantes;

@Service
public class EmailService {

    private Constantes constantes = new Constantes();

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void enviarEmail(String destinatario, String assunto, String corpo) {
        log.info("Enviando email para " + destinatario + " com assunto " + assunto);

        Properties props = new Properties();

        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enabled", "false");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(constantes.EMAIL, constantes.SENHA_EMAIL);
                    }
                });

        /** Ativa Debug para sessão */
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(constantes.EMAIL));

            Address destino = new InternetAddress(destinatario);

            message.setRecipient(Message.RecipientType.TO, destino);
            message.setSubject(assunto);
            message.setText(corpo);

            /** Método para enviar a mensagem criada */
            Transport.send(message);

            log.info("Email enviado com sucesso");

        } catch (MessagingException e) {
            log.error("Erro ao enviar email", e);

            throw new RuntimeException(e);
        }
    }
}
