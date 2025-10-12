package api.example.SimpleNotes.domain.user.dto.response;

import api.example.SimpleNotes.domain.user.User;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String email,
        String name
) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName());
    }
}
