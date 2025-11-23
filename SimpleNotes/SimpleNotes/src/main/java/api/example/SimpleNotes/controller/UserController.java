package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.user.dto.request.UserRequest;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerencia a busca e manipulação de perfis de usuário.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<UserResponse>> findAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @AuthenticationPrincipal User currentUser
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(service.findAll(pageable, currentUser.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        User user = service.findById(id);
        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal User currentUser,
                                               @RequestBody @Valid UserRequest request) {
        User user = service.update(currentUser.getId(), request);
        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User currentUser) {
        service.delete(currentUser.getId());

        return ResponseEntity.noContent().build();
    }
}
