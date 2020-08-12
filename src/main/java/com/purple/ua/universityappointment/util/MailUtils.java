package com.purple.ua.universityappointment.util;

import com.purple.ua.universityappointment.exception.EmailSendingException;
import com.purple.ua.universityappointment.service.impl.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
public class MailUtils {

    private final Environment environment;

    private final EmailSenderService emailSenderService;

    public String getUrl() {
        String port = environment.getProperty("server.port");
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new EmailSendingException(HttpStatus.FAILED_DEPENDENCY, "Failed sending email due to: "
                    + e.getMessage());
        }
        return "https://" + ip + ":" + port;
    }

    public void mailInput(String email, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        emailSenderService.sendEmail(mailMessage);
    }
}
