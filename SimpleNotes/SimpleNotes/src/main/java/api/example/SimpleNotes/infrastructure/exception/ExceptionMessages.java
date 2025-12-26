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
    TOKEN_IS_USED("Token já utilizado"),
    EMAIL_NOT_CONFIRMED("Email não confirmado, verifique sua caixa de email"),
    NOTIFICATION_NOT_FOUND("Notificação não encontrada"),
    FRIEND_REQUEST_ALREADY_PENDING("Você já possui um pedido de amizade pendente para este usuário."),
    CANNOT_SEND_REQUEST_TO_YOURSELF("Você não pode enviar um pedido para si mesmo."),
    FRIEND_REQUEST_NOT_FOUND("Solicitação de amizade não encontrada"),
    FRIEND_REQUEST_ALREADY_ANSWERED("Solicitação de amizade já respondida"),
    WALLET_NOT_FOUND("Cofre não encontrado"),
    USER_IS_MEMBER_OF_WALLET("Usuário já é membro do cofre"),
    INTERNAL_SERVER_ERROR("Erro interno do servidor"),
    NOTE_NOT_FOUND("Nota não encontrada"),
    WALLET_USER_NOT_FOUND("Permissão do usuario não encontrada");



    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

}
