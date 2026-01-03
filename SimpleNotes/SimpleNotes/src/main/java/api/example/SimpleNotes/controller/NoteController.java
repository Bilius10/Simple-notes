package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.note.Note;
import api.example.SimpleNotes.domain.note.NoteService;
import api.example.SimpleNotes.domain.note.dto.request.NoteCreate;
import api.example.SimpleNotes.domain.note.dto.request.NoteUpdate;
import api.example.SimpleNotes.domain.note.dto.request.UploadFile;
import api.example.SimpleNotes.domain.note.dto.response.NoteResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/note")
@Tag(name = "Notas", description = "Gerencia as notas dentro de uma carteira (Wallet) e requer permissão explícita.")
@SecurityRequirement(name = "bearerAuth")
public class NoteController {

    private  final NoteService noteService;

    @PostMapping
    @PreAuthorize("hasRole('USER') and @wus.verifyHasPermission(#request.walletId, principal.id, T(api.example.SimpleNotes.domain.wallet_user.WalletPermission).CREATE)")
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteCreate request, @AuthenticationPrincipal User currentUser) {
        Note note = noteService.create(request.title(), request.content(), request.walletId(), currentUser.getId());
        NoteResponse response = new NoteResponse(note);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{walletId}")
    @PreAuthorize("hasRole('USER') and @wus.verifyHasPermission(#walletId, principal.id, T(api.example.SimpleNotes.domain.wallet_user.WalletPermission).VIEW)")
    public ResponseEntity<PageDTO<NoteResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "title", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @PathVariable Long walletId
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageDTO<NoteResponse> response = noteService.findAll(walletId, pageable);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('USER') and @wus.verifyHasPermission(#walletId, principal.id, T(api.example.SimpleNotes.domain.wallet_user.WalletPermission).DELETE)")
    public ResponseEntity<Void> delete(@PathVariable Long noteId, @RequestParam Long walletId) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{noteId}")
    @PreAuthorize("hasRole('USER') and @wus.verifyHasPermission(#walletId, principal.id, T(api.example.SimpleNotes.domain.wallet_user.WalletPermission).UPDATE)")
    public ResponseEntity<NoteResponse> update(@PathVariable Long noteId, @Valid @RequestBody NoteUpdate noteUpdate, @RequestParam Long walletId) {
        Note note = noteService.update(noteId, noteUpdate.title(), noteUpdate.content());
        NoteResponse response = new NoteResponse(note);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoteResponse> uploadFile(@Valid @ModelAttribute UploadFile request, @AuthenticationPrincipal User currentUser) {
        Note note = noteService.uploadFile(request.file(), request.walletId(), currentUser.getId());
        NoteResponse response = new NoteResponse(note);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
