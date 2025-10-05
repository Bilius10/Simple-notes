package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.user.dto.request.LoginRequestUser;
import api.example.SimpleNotes.domain.user.dto.request.RegisterUser;
import api.example.SimpleNotes.domain.user.dto.request.ResetPassword;
import api.example.SimpleNotes.domain.user.dto.response.LoginResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUser request) {
        service.register(request.email(), request.name(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseUser> login(@RequestBody @Valid LoginRequestUser request) {
        LoginResponseUser response = service.login(request.email(), request.password());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
        service.confirmEmail(token);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {

        service.forgotPassword(email);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPassword request, @RequestParam String token) {
        service.resetPassword(token, request.newPassword());

        return ResponseEntity.noContent().build();
    }
}
