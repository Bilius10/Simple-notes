package api.example.SimpleNotes.domain.wallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WalletRequest(
        @NotNull(message = "wallet.name.required")
        @NotBlank(message = "wallet.name.required")
        @Size(max = 100, message = "wallet.name.size")
        String name,

        @NotNull(message = "wallet.description.required")
        @NotBlank(message = "wallet.description.required")
        @Size(max = 100, message = "wallet.description.size")
        String description
) {
}
