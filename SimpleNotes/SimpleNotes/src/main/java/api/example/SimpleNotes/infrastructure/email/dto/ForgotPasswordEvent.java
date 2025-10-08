package api.example.SimpleNotes.infrastructure.email.dto;

public record ForgotPasswordEvent(
        String email,
        String token,
        String name
) {
}
