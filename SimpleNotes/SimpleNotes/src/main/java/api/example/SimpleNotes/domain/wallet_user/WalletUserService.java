package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletRepository;
import api.example.SimpleNotes.domain.wallet.WalletService;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.USER_IS_MEMBER_OF_WALLET;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.WALLET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WalletUserService {

    private final WalletUserRepository walletUserRepository;
    private final UserService userService;
    private final WalletRepository walletRepository;

    @Transactional
    public WalletUser create(Long walletId, Long userId, Boolean canCreate, Boolean canUpdate, Boolean canDelete, Boolean canView) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ServiceException(WALLET_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        User user = userService.findById(userId);

        if (walletUserRepository.existsByWalletAndUser(wallet, user)) {
            throw new ServiceException(USER_IS_MEMBER_OF_WALLET.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        WalletUser walletUser = new WalletUser(wallet, user, canCreate, canUpdate, canDelete, canView);

        return walletUserRepository.save(walletUser);
    }
}
