package api.example.SimpleNotes.domain.friend_request;

public enum FriendRequestStatus {

    WAITING("aguardando"),
    REFUSED("recusado"),
    ACCEPTED("aceito");

    private String friendRequestStatus;

    FriendRequestStatus(String friendRequestStatus) {
        this.friendRequestStatus = friendRequestStatus;
    }
}
