package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.friend_request.FriendRequestService;
import api.example.SimpleNotes.domain.friend_request.dto.request.FriendRequestCreateRequest;
import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend-request")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FriendRequestResponse> create(@RequestBody @Valid FriendRequestCreateRequest friendRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(friendRequest.senderId(), friendRequest.receiverId()));
    }
}
