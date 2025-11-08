package api.example.SimpleNotes.controller;

import api.example.SimpleNotes.domain.note.Note;
import api.example.SimpleNotes.domain.note.NoteService;
import api.example.SimpleNotes.domain.note.dto.request.NoteCreate;
import api.example.SimpleNotes.domain.note.dto.response.NoteResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/note")
public class NoteController {

    private  final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteCreate noteCreate,
                                               @AuthenticationPrincipal User authenticatedUser) {
        Note note = noteService.create(noteCreate.title(), noteCreate.content(), noteCreate.walletId(), authenticatedUser.getId());
        NoteResponse response = new NoteResponse(note);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<PageDTO<NoteResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "title", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending,
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable Long walletId
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageDTO<NoteResponse> response = noteService.findAll(walletId, authenticatedUser.getId(), pageable);

        return ResponseEntity.ok().body(response);
    }
}
