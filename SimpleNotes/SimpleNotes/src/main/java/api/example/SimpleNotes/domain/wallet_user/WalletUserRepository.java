package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface WalletUserRepository extends JpaRepository<WalletUser,Long> {

    @Transactional(readOnly = true)
    boolean existsByWalletAndUser(Wallet wallet, User user);

    @Transactional(readOnly = true)
    Optional<WalletUser> findByWalletIdAndUserId(Long walletId, Long userId);
}
