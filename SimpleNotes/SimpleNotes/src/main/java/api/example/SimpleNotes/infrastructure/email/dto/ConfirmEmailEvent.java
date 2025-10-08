package api.example.SimpleNotes.infrastructure.email.dto;

public record ConfirmEmailEvent(
        String email,
        String token,
        String name
) {
}
