package com.tfg.pmh.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender sender;

    @Override
    public void sendEmail(Email emailBody)  {
        LOGGER.info("EmailBody: {}", emailBody.toString());
        sendEmailTool(emailBody.getContent(), emailBody.getEmail(), emailBody.getSubject());
    }


    private void sendEmailTool(String textMessage, String email, String subject) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setText(textMessage, true);
            helper.setSubject(subject);
            sender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        }
    }
}
