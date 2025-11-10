package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletRepository;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.USER_IS_MEMBER_OF_WALLET;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.WALLET_NOT_FOUND;

@Service("wus")
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

    @Transactional(readOnly = true)
    public boolean verifyHasPermission(Long walletId, Long userId, WalletPermission permission) {
        WalletUser walletUser = walletUserRepository.findByWalletIdAndUserId(walletId, userId).orElse(null);

        if (walletUser == null) {
            return false;
        }

        return switch (permission) {
            case CREATE -> walletUser.getCanCreate();
            case UPDATE -> walletUser.getCanUpdate();
            case DELETE -> walletUser.getCanDelete();
            case VIEW -> walletUser.getCanView();
        };
    }
}
