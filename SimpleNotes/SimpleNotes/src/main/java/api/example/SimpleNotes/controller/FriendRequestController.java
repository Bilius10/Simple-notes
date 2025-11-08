package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.friend_request.FriendRequestService;
import api.example.SimpleNotes.domain.friend_request.dto.request.AddFriendRequest;
import api.example.SimpleNotes.domain.friend_request.dto.request.FriendRequestCreateRequest;
import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestListResponse;
import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/friend-request")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;

    @GetMapping("friends")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<FriendRequestListResponse>> findAllFriends(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(service.findAllFriends(authenticatedUser.getId(), pageable));
    }

    @GetMapping("pendings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<FriendRequestListResponse>> findAllPendingFriendRequests(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(service.findAllPendingFriendRequests(authenticatedUser.getId(), pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FriendRequestResponse> create(@RequestBody @Valid FriendRequestCreateRequest friendRequest) {
        FriendRequestResponse response = service.create(friendRequest.senderId(), friendRequest.receiverId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable long id, @AuthenticationPrincipal User authenticatedUser) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FriendRequestResponse> addFriend(@PathVariable long id, @RequestBody @Valid AddFriendRequest addFriendRequest) {
        FriendRequestResponse response = service.addFriend(id, addFriendRequest.status());

        return ResponseEntity.ok().body(response);
    }
}
