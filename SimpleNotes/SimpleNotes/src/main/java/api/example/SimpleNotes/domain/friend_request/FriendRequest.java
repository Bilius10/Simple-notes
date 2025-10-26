package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend_request")
@EntityListeners(AuditingEntityListener.class)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userSender;

    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userReceiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendRequestStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FriendRequest(User userSender, User userReceiver) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.status = FriendRequestStatus.WAITING;
    }
}
