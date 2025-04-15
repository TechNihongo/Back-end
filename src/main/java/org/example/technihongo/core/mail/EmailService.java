package org.example.technihongo.core.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.technihongo.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        message.setFrom("technihongo.work@gmail.com");

        mailSender.send(message);
    }

    public void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        //String link = "http://localhost:3000/api/user/verify-email?token=" + token;
        String link = "https://technihongo.vercel.app/verify/" + token;
        String subject = "Xác nhận tài khoản email";
        String body = "Vui lòng nhấn vào link để xác nhận tài khoản của bạn: " + link;

        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("technihongo.work@gmail.com");

        mailSender.send(message);
    }

    public void sendReminderEmail(Student student) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(student.getUser().getEmail());
            helper.setSubject("Nhắc nhở học tập hàng ngày");
            helper.setText(
                    String.format(
                            "Chào %s,<br><br>" +
                                    "Đã đến giờ học của bạn! Mục tiêu hôm nay là %d phút.<br>" +
                                    "Chúc bạn học tập hiệu quả!<br><br>" +
                                    "Trân trọng,<br>TechNihongo Team",
                            student.getUser().getUserName(),
                            student.getDailyGoal()
                    ),
                    true
            );

            mailSender.send(message);
        } catch (MessagingException ignored) {
        }
    }
}
