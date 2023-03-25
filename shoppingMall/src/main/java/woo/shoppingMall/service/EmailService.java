package woo.shoppingMall.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import woo.shoppingMall.domain.user.User;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private String authNumber = "";

    public void createCode() {
        authNumber = UUID.randomUUID().toString().split("-")[0].toUpperCase(Locale.ROOT);
    }

    public User findId(Long userId) {
        return userService.findOne(userId).orElse(null);
    }
    public MimeMessage createEmailForm(String email) throws MessagingException {
        createCode();
        String sender = "sdjf234@naver.com";
        String title = "슈즈스토리 회원가입 인증번호";

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(sender);
        message.setText(setContext(authNumber), "utf-8", "html");
        return message;
    }
    public MimeMessage findUserLoginId(String email, Long userId) throws MessagingException {
        User user = findId(userId);
        String userLoginId = user.getUserLoginId();
        String sender = "sdjf234@naver.com";
        String title = "슈즈스토리 아이디";

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(sender);
        message.setText(setUserLoginId(userLoginId), "utf-8", "html");
        return message;
    }
    public MimeMessage findUserPassword(String email, Long userId) throws MessagingException {
        User user = findId(userId);
        String userPassword = user.getUserPassword();
        String sender = "sdjf234@naver.com";
        String title = "슈즈스토리  비밀번호";

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(sender);
        message.setText(setUserPassword(userPassword), "utf-8", "html");
        return message;
    }
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(email);
        javaMailSender.send(emailForm);

        return authNumber;
    }
    public void sendUserIdEmail(String email, Long userId) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = findUserLoginId(email, userId);
        javaMailSender.send(emailForm);
    }
    public void sendUserPasswordEmail(String email, Long userId) throws MessagingException{
        MimeMessage emailForm = findUserPassword(email, userId);
        javaMailSender.send(emailForm);
    }
    private String setContext(String authNumber) {
        Context context = new Context();
        context.setVariable("authNumber", authNumber);
        return springTemplateEngine.process("mail/mail", context);
    }
    private String setUserLoginId(String userLoginId) {
        Context context = new Context();
        context.setVariable("userLoginId", userLoginId);
        return springTemplateEngine.process("mail/findIdMail", context);
    }
    private String setUserPassword(String userPassword) {
        Context context = new Context();
        context.setVariable("userPassword", userPassword);
        return springTemplateEngine.process("mail/findPasswordMail", context);
    }
}
