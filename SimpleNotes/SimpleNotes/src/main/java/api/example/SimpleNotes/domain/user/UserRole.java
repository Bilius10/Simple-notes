package api.example.SimpleNotes.domain.user;

public enum UserRole {

    ADMIN("Administrador"),
    USER("usuario");

    private String userRole;

    UserRole(String userRole) {
        this.userRole = userRole;
    }
}
