package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_IS_USED;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_NOT_VALID;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder encoder;

    public void saveUserToken(User user, String token, TokenType tokenType) {
        String tokenEnconder = encoder.encode(token);

        UserToken userToken = new UserToken(user, tokenEnconder, tokenType);

        userTokenRepository.save(userToken);
    }

    public User validateAndUseToken(String rawToken) {
        String tokenHash = encoder.encode(rawToken);

        UserToken userToken = userTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST));

        if (userToken.getIsUsed()) {
            throw new ServiceException(TOKEN_IS_USED.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Duration duration = Duration.between(userToken.getCreatedAt(), Instant.now());

        if (duration.toHours() > 24) {
            throw new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
        }

        userToken.setIsUsed(true);
        userTokenRepository.save(userToken);

        return  userToken.getUser();
    }
}
