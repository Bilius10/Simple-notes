package api.example.SimpleNotes.domain.wallet_user;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletRepository;
import api.example.SimpleNotes.domain.wallet.dto.response.WalletResponse;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;
import api.example.SimpleNotes.domain.wallet_user.dto.response.ListWalletUser;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import api.example.SimpleNotes.infrastructure.security.AuditorAwareImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.*;

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

    @Transactional
    public WalletUser update(Long id, PermissionRequest permission) {
        WalletUser walletUser = findById(id);

        walletUser.updatePermission(permission);

        return walletUserRepository.save(walletUser);
    }

    @Transactional
    public void delete(Long id) {
        WalletUser walletUser = findById(id);

        walletUserRepository.delete(walletUser);
    }

    public PageDTO<ListWalletUser> findAllByWalletId(Long walletId, Pageable pageable) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        Page<WalletUser> walletsUser = walletUserRepository.findAllByCreatedByAndWalletId(currentAuditor.get(), walletId, pageable);
        Page<ListWalletUser> record = walletsUser.map(ListWalletUser::new);

        return new PageDTO<>(record);
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

    private WalletUser findById(Long id) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

         return walletUserRepository.findByIdAndCreatedBy(id, currentAuditor.get())
                .orElseThrow(() -> new ServiceException(WALLET_USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }
}
