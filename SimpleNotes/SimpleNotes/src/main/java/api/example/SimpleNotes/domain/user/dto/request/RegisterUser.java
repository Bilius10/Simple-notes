package api.example.SimpleNotes.domain.user.dto.request;

import api.example.SimpleNotes.infrastructure.annotation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUser(

        @NotNull(message = "{user.email.required}")
        @NotBlank(message = "{user.email.required}")
        @Size(max = 100, message = "{user.email.size}")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "{user.email.invalid.format}")
        String email,

        @NotNull(message = "{user.password.required}")
        @NotBlank(message = "{user.password.required}")
        @StrongPassword
        @Size(min = 8, max = 20, message = "{user.password.size}")
        String password,

        @NotNull(message = "{user.name.required}")
        @NotBlank(message = "{user.name.required}")
        @Size(max = 100, message = "{user.name.size}")
        String name
) {
}
