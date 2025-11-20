package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "wallet_user")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
public class WalletUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "canCreate")
    private Boolean canCreate;

    @Column(name = "canUpdate")
    private Boolean canUpdate;

    @Column(name = "canDelete")
    private Boolean canDelete;

    @Column(name = "canView", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean canView;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public WalletUser(Wallet wallet, User user, PermissionRequest permission) {
        this.wallet = wallet;
        this.user = user;
        this.canCreate = permission.canCreate();
        this.canUpdate = permission.canUpdate();
        this.canDelete = permission.canDelete();
    }
}
