package api.example.SimpleNotes.infrastructure.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    TOKEN_NOT_GENERATED("Erro ao gerar token"),
    TOKEN_NOT_VALID("Token inválido ou expirado"),
    USER_NOT_FOUND("Usuário não encontrado"),
    EMAIL_IN_USE("Email já cadastrado"),
    NAME_IN_USE("Nome de usuário já cadastrado"),
    INVALID_CREDENTIALS("Credenciais inválidas"),
    TOKEN_IS_USED("Token já utilizado");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

}
