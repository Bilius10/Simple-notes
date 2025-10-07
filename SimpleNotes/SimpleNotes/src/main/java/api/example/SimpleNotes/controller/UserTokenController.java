package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.user_token.UserTokenScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-token")
@RequiredArgsConstructor
public class UserTokenController {

    private final UserTokenScheduler service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExpiredTokens() {
        service.deleteExpiredTokens();

        return ResponseEntity.ok().build();
    }


}
