package api.example.SimpleNotes.infrastructure.email;

import api.example.SimpleNotes.infrastructure.email.dto.ConfirmEmailEvent;
import api.example.SimpleNotes.infrastructure.email.dto.ForgotPasswordEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmailEventListeners {

    private final SpringMailSenderService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleConfirmEmailEvent(ConfirmEmailEvent event) {
        emailService.sendConfirmEmail(event.email(), event.name(), event.token());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleForgotPasswordEvent(ForgotPasswordEvent event) {
        emailService.sendForgotPasswordEmail(event.email(), event.name(), event.token());
    }
}
