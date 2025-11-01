package api.example.SimpleNotes.domain.wallet.dto.response;

import api.example.SimpleNotes.domain.wallet.Wallet;

public record WalletResponse(
        Long id,
        String name,
        String description
) {

    public WalletResponse(Wallet wallet) {
        this(wallet.getId(), wallet.getName(), wallet.getDescription());
    }
}
