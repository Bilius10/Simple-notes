package api.example.SimpleNotes.domain.user_token;

import api.example.SimpleNotes.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "user_tokens")
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "is_used")
    private Boolean isUsed;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UserToken(User user, String tokenHash, TokenType tokenType) {
        this.user = user;
        this.isUsed = false;
        this.tokenHash = tokenHash;
        this.tokenType = tokenType;
    }
}
