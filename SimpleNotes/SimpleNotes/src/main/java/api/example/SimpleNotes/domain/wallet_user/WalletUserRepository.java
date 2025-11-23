package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.wallet.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletUserRepository extends JpaRepository<WalletUser,Long> {

    @Transactional(readOnly = true)
    boolean existsByWalletAndUser(Wallet wallet, User user);

    @Transactional(readOnly = true)
    Optional<WalletUser> findByWalletIdAndUserId(Long walletId, Long userId);

    @Transactional(readOnly = true)
    Optional<WalletUser> findByIdAndCreatedBy(Long walletId, String createdBy);

    @Transactional(readOnly = true)
    Page<WalletUser> findAllByCreatedByAndWalletId(String createdBy, Long walletId, Pageable pageable);
}
