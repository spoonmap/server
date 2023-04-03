package xyz.spoonmap.server.member.aop;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PasswordMailAspect {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @AfterReturning(
        value = "execution(* xyz.spoonmap.server.member.service.MemberService.findPassword(..))",
        returning = "returnValue"
    )
    public void sendInitializePasswordMail(PasswordUpdateResponse returnValue) {
        MimeMessage message = javaMailSender.createMimeMessage();

        setMail(message, returnValue.email(), returnValue.updatedPassword());
        javaMailSender.send(message);

        log.info("Success to send initialize password mail to [{}]", returnValue.email());
    }

    private void setMail(MimeMessage message, String email, String password) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");
            messageHelper.setTo(email);
            messageHelper.setSubject("Spoonmap 비밀번호 초기화");
            messageHelper.setText(setMailContent(email, password), true);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    private String setMailContent(String email, String password) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("changedPassword", password);
        return springTemplateEngine.process("mail/member/password", context);
    }

}
