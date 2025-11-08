package api.example.SimpleNotes.domain.wallet_user;

public enum WalletPermission {
    CREATE("criar"),
    UPDATE("atualizar"),
    DELETE("deletar"),
    VIEW("visualizar");

    private String permission;

    WalletPermission(String permission) {
        this.permission = permission;
    }

}
