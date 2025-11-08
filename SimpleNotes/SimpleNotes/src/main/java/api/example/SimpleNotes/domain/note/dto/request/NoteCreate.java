package api.example.SimpleNotes.domain.note.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteCreate(
        @NotNull(message = "{note.title.required}")
        @NotBlank(message = "{note.title.required}")
        @Size(max = 50, message = "{note.title.size}")
        String title,

        @NotNull(message = "{note.content.required}")
        @NotBlank(message = "{note.content.required}")
        @Size(max = 50, message = "{note.content.size}")
        String content,

        @NotNull(message = "{note.walletId.notNull}")
        Long walletId
) {
}
