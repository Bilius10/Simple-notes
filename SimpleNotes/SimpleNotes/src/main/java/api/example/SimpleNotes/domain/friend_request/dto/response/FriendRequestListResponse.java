package api.example.SimpleNotes.domain.friend_request.dto.response;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;

public record FriendRequestListResponse(
        Long id,
        UserResponse userResponse
) {
    public FriendRequestListResponse(Long id, User user) {
        this(id, new UserResponse(user));
    }
}
