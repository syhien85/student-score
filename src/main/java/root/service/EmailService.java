package root.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine; // tạo @Bean: Thymeleaf

    public void sendBirthdayEmail(String to, String name) {
        String subject = "Happy Birthday ! " + name;

        /**
         * Context như Model (MVC)
         */
        Context context = new Context();
        context.setVariable("name", name); // truyen bien ${name} sang email/happy-birthday.html

        // đọc file html set content vào body email:
        String body = templateEngine.process("email/happy-birthday.html", context);

        sendMail(to, subject, body);
    }

    public void sendRegisterEmail(String to, String name) {
        String subject = "Happy Birthday ! " + name;
        Context context = new Context();
        context.setVariable("name", name);
        String body = templateEngine.process("email/register.html", context);

        sendMail(to, subject, body);
    }

    // test
    public void testEmail() {
        String to = "syhien085@yahoo.com"; // mail nguoi nhan
        String subject = "Tieu de email";
        String body = "<h1>Noi dung email</h1>";

        sendMail(to, subject, body);
    }

    // test
    private void sendMail(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true: hỗ trợ đọc html body email
            // helper.setFrom("syhien85@gmail.com"); // mail gui

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
