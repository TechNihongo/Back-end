package org.example.technihongo.core.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("songlongdiamond105@gmail.com");

        mailSender.send(message);
    }

    public void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        //String link = "http://localhost:3000/api/user/verify-email?token=" + token;
        String link = "https://technihongo-lwh1edw2l-lams-projects-496a2108.vercel.app/verify/" + token;
        String subject = "Verify your email";
        String body = "Click the link to verify your email: " + link;

        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("songlongdiamond105@gmail.com");

        mailSender.send(message);
    }

}
