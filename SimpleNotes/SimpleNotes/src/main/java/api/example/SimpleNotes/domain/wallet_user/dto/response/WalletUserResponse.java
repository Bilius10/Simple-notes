package api.example.SimpleNotes.domain.wallet_user.dto.response;

import api.example.SimpleNotes.domain.wallet_user.WalletUser;
import api.example.SimpleNotes.domain.wallet_user.dto.request.PermissionRequest;

public record WalletUserResponse(
        Long id,
        Long userId,
        Long walletId,
        PermissionRequest permission
) {

    public WalletUserResponse(WalletUser walletUser) {
        this(walletUser.getId(), walletUser.getUser().getId(), walletUser.getWallet().getId(), new PermissionRequest(walletUser));
    }
}
