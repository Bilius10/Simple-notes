package api.example.SimpleNotes.infrastructure.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Service
@Async
@RequiredArgsConstructor
public class SpringMailSenderService {

    private static final Logger log = LoggerFactory.getLogger(SpringMailSenderService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendConfirmEmail(String email, String name, String token) {
        String subject = "Confirmação de cadastro";
        String templateName = "confirm-email";

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("verificationLink", "http://localhost:4200/confirm-email/" + token);

        sendEmailWithTemplate(email, subject, templateName, variables);
    }

    public void sendForgotPasswordEmail(String email, String name, String token) {
        String subject = "Redefinição de senha";
        String templateName = "forgot-password-email";

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("resetLink", "http://localhost:4200/reset-password/" + token);

        sendEmailWithTemplate(email, subject, templateName, variables);
    }

    @Retryable(
            value = { MailException.class, MessagingException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    private void sendEmailWithTemplate(String to, String subject, String templateName,
                                       Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            log.info("Enviando e-mail com template '{}' para {}", templateName, to);
            mailSender.send(mimeMessage);
            log.info("E-mail enviado com sucesso para {}", to);

        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail para {}: {}", to, e.getMessage());
        }
    }

    @Recover
    private void recoverFromEmailError(Exception e,
                                       String to,
                                       String subject,
                                       String templateName,
                                       Map<String, Object> variables) {
        log.error("Falha ao enviar e-mail para {} após 3 tentativas: {}", to, e.getMessage());
    }
}
