package api.example.SimpleNotes.domain.wallet_user.dto.request;

import api.example.SimpleNotes.domain.wallet_user.WalletUser;

public record PermissionRequest(
        Boolean canCreate,
        Boolean canUpdate,
        Boolean canDelete
) {
    public PermissionRequest(WalletUser walletUser) {
        this(walletUser.getCanCreate(), walletUser.getCanUpdate(), walletUser.getCanDelete());
    }

    public PermissionRequest(Boolean canCreate, Boolean canUpdate, Boolean canDelete) {
        this.canCreate = canCreate;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
    }
}
