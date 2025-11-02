package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletService;
import api.example.SimpleNotes.domain.wallet.dto.request.WalletRequest;
import api.example.SimpleNotes.domain.wallet.dto.response.WalletResponse;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
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
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService service;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<WalletResponse>> findAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(service.findAll(pageable, authenticatedUser.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WalletResponse> create(@AuthenticationPrincipal User authenticatedUser, @Valid @RequestBody WalletRequest request) {
        Wallet wallet = service.create(request.name(), request.description(), authenticatedUser.getId());
        WalletResponse walletResponse = new WalletResponse(wallet);

        return ResponseEntity.ok().body(walletResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WalletResponse> get(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id) {
        Wallet wallet = service.findById(id, authenticatedUser.getId());
        WalletResponse walletResponse = new WalletResponse(wallet);

        return ResponseEntity.ok().body(walletResponse);
    }
}
