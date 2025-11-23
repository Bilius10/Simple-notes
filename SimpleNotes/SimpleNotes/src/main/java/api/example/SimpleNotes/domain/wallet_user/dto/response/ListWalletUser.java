package api.example.SimpleNotes.domain.wallet_user.dto.response;

import api.example.SimpleNotes.domain.wallet_user.WalletUser;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;

public record ListWalletUser(
        Long id,
        String username,
        PermissionRequest permission
) {
    public ListWalletUser(WalletUser walletUser) {
        this(walletUser.getId(), walletUser.getUser().getName(), new PermissionRequest(walletUser));
    }
}
