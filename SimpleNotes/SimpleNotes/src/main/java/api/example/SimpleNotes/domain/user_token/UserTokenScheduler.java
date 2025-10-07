package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.infrastructure.email.SpringMailSenderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTokenScheduler {

    private static final Logger log = LoggerFactory.getLogger(UserTokenScheduler.class);

    private final UserTokenRepository repository;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredTokens() {
        List<UserToken> tokens = repository.findAllByIsUsedIsFalse();

        LocalDateTime currentTime = LocalDateTime.now();

        tokens.forEach(token -> {
            long hoursElapsed = Duration.between(token.getCreatedAt(), currentTime).toHours();

            if (hoursElapsed > 24) {
                log.info("Deletando token com tempo vencido: " + token.getId());
                repository.delete(token);
            }
        });
    }
}
