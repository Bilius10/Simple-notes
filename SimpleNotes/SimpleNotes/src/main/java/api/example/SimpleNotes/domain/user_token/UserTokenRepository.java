package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    List<UserToken> findByUserAndTokenTypeAndIsUsedIsTrue(User user, TokenType tokenType);

    Optional<UserToken> findByTokenHash(String tokenHash);

    List<UserToken> findAllByIsUsedIsFalse();
}
