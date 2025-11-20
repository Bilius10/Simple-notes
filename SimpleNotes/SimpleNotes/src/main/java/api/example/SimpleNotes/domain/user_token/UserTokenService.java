package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_IS_USED;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_NOT_VALID;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public UserToken saveUserToken(User user, TokenType tokenType) {
        String token = UUID.randomUUID().toString();

        UserToken userToken = new UserToken(user, token, tokenType);

        return repository.save(userToken);
    }

    @Transactional(readOnly = true)
    public User validateAndUseToken(String rawToken) {

        UserToken userToken = findTokenByHash(rawToken);

        validateToken(userToken);

        userToken.setIsUsed(true);
        repository.save(userToken);

        return userToken.getUser();
    }

    private UserToken findTokenByHash(String rawToken) {
        return repository.findByTokenHash(rawToken)
                .orElseThrow(() -> new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST));
    }

    private void validateToken(UserToken userToken) {
        if (userToken.getIsUsed()) {
            throw new ServiceException(TOKEN_IS_USED.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (isTokenExpired(userToken)) {
            throw new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isTokenExpired(UserToken userToken) {
        long hoursSinceCreation = Duration.between(userToken.getCreatedAt(), LocalDateTime.now()).toHours();
        return hoursSinceCreation > 24;
    }
}
