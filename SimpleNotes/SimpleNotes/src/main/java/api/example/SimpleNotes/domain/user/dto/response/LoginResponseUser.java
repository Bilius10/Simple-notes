package api.example.SimpleNotes.domain.user.dto.response;

import api.example.SimpleNotes.domain.user.User;
import lombok.Builder;

@Builder
public record LoginResponseUser(
        Long id,
        String email,
        String name,
        String token
) {

    public LoginResponseUser(User user, String token) {
        this(user.getId(), user.getEmail(), user.getName(), token);
    }

}
