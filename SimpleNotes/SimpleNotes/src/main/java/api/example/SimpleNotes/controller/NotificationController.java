package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.notification.NotificationService;
import api.example.SimpleNotes.domain.notification.dto.response.NotificationResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<NotificationResponse>> findAll(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "message", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(service.findAll(pageable, authenticatedUser.getId()));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> countUnreadNotifications(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        Long count = service.countUnreadNotifications(authenticatedUser.getId());
        return ResponseEntity.ok().body(count);
    }

    @PatchMapping("/mark-as-read/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        service.markAsRead(id, authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/mark-all-as-read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal User authenticatedUser
    ){
        service.markAllAsRead(authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }
}