package api.example.SimpleNotes.domain.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    @Query("""
            SELECT w FROM Wallet w
            JOIN WalletUser wu ON wu.wallet.id = w.id
            WHERE wu.user.id = :userId
            """)
    Page<Wallet> findAllByUserId(Long userId, Pageable pageable);

    Optional<Wallet> findByIdAndCreatedBy(Long id, String createdBy);
}
