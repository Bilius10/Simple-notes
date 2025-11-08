package api.example.SimpleNotes.domain.note;

import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.infrastructure.encrypt.StringEncryptorConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor
@Entity
@Table(name = "note")
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringEncryptorConverter.class)
    @Column(name = "title", length = 100)
    private String title;

    @Convert(converter = StringEncryptorConverter.class)
    @Column(name = "content", columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;

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

    public Note(String title, String content, Wallet wallet) {
        this.title = title;
        this.content = content;
        this.wallet = wallet;
    }
}
