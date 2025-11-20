package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletRepository;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import api.example.SimpleNotes.infrastructure.security.AuditorAwareImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.USER_IS_MEMBER_OF_WALLET;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.WALLET_NOT_FOUND;

@Service("wus")
@RequiredArgsConstructor
public class WalletUserService {

    private final WalletUserRepository walletUserRepository;
    private final UserService userService;
    private final WalletRepository walletRepository;
    private final AuditorAwareImpl auditorAware;

    @Transactional
    public WalletUser create(Long walletId, Long userId, PermissionRequest permission) {
        User user = userService.findById(userId);
        Wallet wallet = getWalletByCurrentAuditor(walletId);

        validateUserIsNotMember(wallet, user);

        WalletUser walletUser = new WalletUser(wallet, user, permission);

        return walletUserRepository.save(walletUser);
    }

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

    private Wallet getWalletByCurrentAuditor(Long walletId) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        return walletRepository.findByIdAndCreatedBy(walletId, currentAuditor.get())
                .orElseThrow(() -> new ServiceException(WALLET_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }

    private void validateUserIsNotMember(Wallet wallet, User user) {
        boolean alreadyExists = walletUserRepository.existsByWalletAndUser(wallet, user);

        if (alreadyExists) {
            throw new ServiceException(USER_IS_MEMBER_OF_WALLET.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
