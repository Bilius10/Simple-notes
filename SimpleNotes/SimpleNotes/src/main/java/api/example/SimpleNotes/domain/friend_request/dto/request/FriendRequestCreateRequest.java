package api.example.SimpleNotes.domain.friend_request.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FriendRequestCreateRequest(
        @NotNull(message = "{friendrequest.idUserSender.notNull}")
        @Positive(message = "{friendrequest.idUserSender.positive}")
        Long senderId,

        @NotNull(message = "{friendrequest.idUserReceiver.notNull}")
        @Positive(message = "{friendrequest.idUserReceiver.positive}")
        Long receiverId
) {
}
