package api.example.SimpleNotes.domain.wallet_user.dto.request;

import jakarta.validation.constraints.NotNull;

public record WalletUserRequest(
        @NotNull(message = "walletuser.walletId.notNull")
        Long walletId,

        @NotNull(message = "walletuser.userId.notNull")
        Long userId,

        PermissionRequest permission
) {
}
