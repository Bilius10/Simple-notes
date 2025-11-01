package api.example.SimpleNotes.domain.wallet;

import api.example.SimpleNotes.domain.wallet.dto.response.WalletResponse;
import api.example.SimpleNotes.domain.wallet_user.WalletUser;
import api.example.SimpleNotes.domain.wallet_user.WalletUserService;
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

    @Transactional(readOnly = true)
    public PageDTO<WalletResponse> findAll(Pageable pageable, Long userId) {
        Page<Wallet> wallets = repository.findAllByUserId(userId, pageable);

        Page<WalletResponse> dtosPage = wallets.map(WalletResponse::new);

        return new PageDTO<>(
                dtosPage.getContent(),
                wallets.getNumber(),
                wallets.getSize(),
                wallets.getTotalElements(),
                wallets.getTotalPages());
    }

    @Transactional
    public Wallet create(String name, String description, Long ownerId) {
        Wallet wallet = new Wallet(name, description);

        Wallet savedWallet = repository.save(wallet);

        WalletUser walletUser
                = walletUserService.create(savedWallet.getId(), ownerId, true, true, true, true);

        return savedWallet;
    }

    @Transactional
    public void delete(Long walletId) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        Wallet wallet = repository.findByIdAndCreatedBy(walletId, currentAuditor.get())
                .orElseThrow(() -> new ServiceException(WALLET_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        repository.delete(wallet);
    }

}
