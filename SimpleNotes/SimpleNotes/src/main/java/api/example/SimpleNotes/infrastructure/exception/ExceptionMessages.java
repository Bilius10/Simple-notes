package api.example.SimpleNotes.infrastructure.exception;

public enum ExceptionMessages {

    TOKEN_NOT_GENERATED("Erro ao gerar token"),
    TOKEN_NOT_VALID("Token inválido ou expirado"),
    USER_NOT_FOUND("Usuário não encontrado");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
