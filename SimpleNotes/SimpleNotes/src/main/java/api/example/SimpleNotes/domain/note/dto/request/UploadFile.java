package api.example.SimpleNotes.domain.note.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadFile(

        @NotNull(message = "{note.file.notNull}")
        MultipartFile file,

        @NotNull(message = "{note.walletId.notNull}")
        Long walletId
) {
}
