package api.example.SimpleNotes.domain.user.dto.response;

import api.example.SimpleNotes.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName(), user.getCreatedBy(), user.getCreatedAt(), user.getUpdatedBy(), user.getUpdatedAt());
    }
}
