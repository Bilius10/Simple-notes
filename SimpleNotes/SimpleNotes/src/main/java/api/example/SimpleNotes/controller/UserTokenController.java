package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.user_token.UserTokenScheduler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-token")
@RequiredArgsConstructor
@Tag(name = "Tokens de Usuário (Admin)", description = "Gerencia a manutenção de tokens de redefinição/confirmação de e-mail.")
@SecurityRequirement(name = "bearerAuth")
public class UserTokenController {

    private final UserTokenScheduler service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExpiredTokens() {
        service.deleteExpiredTokens();

        return ResponseEntity.ok().build();
    }


}
