package api.example.SimpleNotes.domain.wallet.dto.response;

import api.example.SimpleNotes.domain.wallet.Wallet;

import java.time.LocalDateTime;

public record WalletResponse(
        Long id,
        String name,
        String description,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) {

    public WalletResponse(Wallet wallet) {
        this(wallet.getId(), wallet.getName(), wallet.getDescription(), wallet.getCreatedBy(), wallet.getCreatedAt(), wallet.getUpdatedBy(), wallet.getUpdatedAt());
    }
}
