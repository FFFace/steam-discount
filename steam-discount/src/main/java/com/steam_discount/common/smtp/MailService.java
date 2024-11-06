package com.steam_discount.common.smtp;


import com.steam_discount.common.exception.CustomException;
import com.steam_discount.common.exception.errorCode.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String text){
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try{
            emailSender.send(emailForm);
        } catch (RuntimeException e){
            log.error("MailService.sendEmail exception: toEmail={}, title={} text={}", toEmail, title, text);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("Steam-Discount");
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
