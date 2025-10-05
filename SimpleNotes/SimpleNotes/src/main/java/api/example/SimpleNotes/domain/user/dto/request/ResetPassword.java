package api.example.SimpleNotes.domain.user.dto.request;

import api.example.SimpleNotes.infrastructure.annotation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPassword(

        @NotNull(message = "{user.password.required}")
        @NotBlank(message = "{user.password.required}")
        @StrongPassword
        @Size(min = 8, max = 20, message = "{user.password.size}")
        String newPassword
) {
}
