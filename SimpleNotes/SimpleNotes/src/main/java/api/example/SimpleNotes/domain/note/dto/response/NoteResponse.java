package api.example.SimpleNotes.domain.note.dto.response;

import api.example.SimpleNotes.domain.note.Note;

import java.time.LocalDateTime;

public record NoteResponse(
        Long id,
        String title,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) {

    public NoteResponse(Note note) {
        this(note.getId(), note.getTitle(), note.getContent(), note.getCreatedBy(), note.getCreatedAt(), note.getUpdatedBy(), note.getUpdatedAt());
    }
}
