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
import xyz.spoonmap.server.member.dto.response.SignupResponse;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SignupMailAspect {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @AfterReturning(
        value = "execution(* xyz.spoonmap.server.member.service.MemberService.signUp(..))",
        returning = "returnValue"
    )
    public void sendSignupVerifyMail(SignupResponse returnValue) {
        MimeMessage message = javaMailSender.createMimeMessage();

        setMail(message, returnValue.id(), returnValue.email());
        javaMailSender.send(message);

        log.info("Success to send signup mail to [{}]", returnValue.email());
    }

    private void setMail(MimeMessage message, Long id, String email) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");
            messageHelper.setTo(email);
            messageHelper.setSubject("Spoonmap 이메일 인증");
            messageHelper.setText(setMailContent(email, id), true);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    private String setMailContent(String email, Long id) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", id);
        return springTemplateEngine.process("mail/member/signup", context);
    }

}
