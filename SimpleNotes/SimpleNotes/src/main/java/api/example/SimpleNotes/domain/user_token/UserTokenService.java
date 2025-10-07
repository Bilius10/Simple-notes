package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_IS_USED;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_NOT_VALID;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository repository;
    private final PasswordEncoder encoder;

    public void saveUserToken(User user, String token, TokenType tokenType) {

        UserToken userToken = new UserToken(user, token, tokenType);

        repository.save(userToken);
    }

    public User validateAndUseToken(String rawToken) {

        UserToken userToken = repository.findByTokenHash(rawToken)
                .orElseThrow(() -> new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST));

        if (userToken.getIsUsed()) {
            throw new ServiceException(TOKEN_IS_USED.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Duration duration = Duration.between(userToken.getCreatedAt(), LocalDateTime.now());

        if (duration.toHours() > 24) {
            throw new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
        }

        userToken.setIsUsed(true);
        repository.save(userToken);

        return  userToken.getUser();
    }

}
