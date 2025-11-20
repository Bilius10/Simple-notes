package api.example.SimpleNotes.domain.wallet;

import api.example.SimpleNotes.domain.wallet.dto.response.WalletResponse;
import api.example.SimpleNotes.domain.wallet_user.WalletUserService;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import api.example.SimpleNotes.infrastructure.security.AuditorAwareImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.WALLET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final WalletUserService walletUserService;
    private final AuditorAwareImpl auditorAware;

    public PageDTO<WalletResponse> findAll(Pageable pageable, Long userId) {
        Page<Wallet> wallets = repository.findAllByUserId(userId, pageable);

        Page<WalletResponse> record = wallets.map(WalletResponse::new);

        return new PageDTO<>(record);
    }

    @Transactional
    public Wallet create(String name, String description, Long ownerId) {
        Wallet wallet = new Wallet(name, description);

        wallet = repository.save(wallet);

        setPermissionsForOwner(wallet.getId(), ownerId);

        return wallet;
    }

    @Transactional
    public void delete(Long walletId) {
        Wallet wallet = getWalletByCurrentAuditor(walletId);

        repository.delete(wallet);
    }

    public Wallet findById(Long walletId, Long userId) {
        return repository.findWalletByIdAndMemberId(walletId, userId)
                .orElseThrow(() -> new ServiceException(WALLET_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }

    private void setPermissionsForOwner(Long walletId, Long userId) {
        PermissionRequest permission = new PermissionRequest(true, true, true);
        walletUserService.create(walletId, userId, permission);
    }

    private Wallet getWalletByCurrentAuditor(Long walletId) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        return repository.findByIdAndCreatedBy(walletId, currentAuditor.get())
                .orElseThrow(() -> new ServiceException(WALLET_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }
}
