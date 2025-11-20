package api.example.SimpleNotes.domain.wallet_user.dto.request;

public record PermissionRequest(
        Boolean canCreate,
        Boolean canUpdate,
        Boolean canDelete
) {
}
