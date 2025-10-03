package api.example.SimpleNotes.infrastructure.security;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_NOT_GENERATED;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.TOKEN_NOT_VALID;

@Service
public class TokenService {

    private final String secret = "teste";

    private final String issuer = "teste";

    private final long expiration = 8L;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new ServiceException(TOKEN_NOT_GENERATED.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new ServiceException(TOKEN_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(expiration).toInstant(ZoneOffset.of("-03:00"));
    }
}
