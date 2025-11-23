package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.wallet_user.WalletUser;
import api.example.SimpleNotes.domain.wallet_user.WalletUserService;
import api.example.SimpleNotes.domain.wallet_user.dto.request.WalletUserRequest;
import api.example.SimpleNotes.domain.wallet_user.dto.response.ListWalletUser;
import api.example.SimpleNotes.domain.wallet_user.dto.response.WalletUserResponse;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/wallet-user")
@RequiredArgsConstructor
public class WalletUserController {

    private final WalletUserService walletUserService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WalletUserResponse> createWalletUser(@RequestBody @Valid WalletUserRequest request) {
        WalletUser walletUser = walletUserService.create(request.walletId(), request.userId(), request.permission());
        WalletUserResponse response = new WalletUserResponse(walletUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WalletUserResponse> updateWalletUser(@RequestBody @Valid WalletUserRequest request,
                                                               @PathVariable Long id) {
        WalletUser walletUser = walletUserService.update(id, request.permission());
        WalletUserResponse response = new WalletUserResponse(walletUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteWalletUser(@PathVariable Long id) {
        walletUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{walletId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDTO<ListWalletUser>> findAllByWalletId(
            @PathVariable Long walletId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok().body(walletUserService.findAllByWalletId(walletId, pageable));
    }
}
