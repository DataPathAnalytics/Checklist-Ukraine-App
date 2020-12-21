package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.exception.MailException;
import com.datapath.checklistukraineapp.util.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    private static final String TOOL_NAME = "Ukraine checklist tool";

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String FROM;

    public void send(MessageTemplate template) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.displayName());

            message.setSubject(template.getSubject());
            message.setTo(template.getEmail());
            message.setFrom(FROM, TOOL_NAME);
            message.setText(template.getText(), false);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailException("Mail not sent");
        }
    }
}



