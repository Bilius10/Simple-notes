package api.example.SimpleNotes.domain.note;

import api.example.SimpleNotes.domain.note.dto.response.NoteResponse;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletService;
import api.example.SimpleNotes.domain.wallet_user.WalletPermission;
import api.example.SimpleNotes.domain.wallet_user.WalletUserService;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.exception.ExceptionMessages;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final WalletService walletService;
    private final WalletUserService walletUserService;
    
    public Note create(String title, String content, Long walletId, Long userId) {
        Wallet wallet = walletService.findById(walletId, userId);

        Note note = new Note(title, content, wallet);

        return noteRepository.save(note);
    }

    public PageDTO<NoteResponse> findAll(Long walletId, Pageable pageable) {
        Page<Note> notes = noteRepository.findAllByWalletId(walletId, pageable);

        Page<NoteResponse> dtosPage = notes.map(NoteResponse::new);

        return new PageDTO<>(
                dtosPage.getContent(),
                notes.getNumber(),
                notes.getSize(),
                notes.getTotalElements(),
                notes.getTotalPages());

    }

    public void delete(Long noteId) {
        Note note = findById(noteId);

        noteRepository.delete(note);
    }

    public Note update(Long noteId, String title, String content) {
        Note note = findById(noteId);

        note.setTitle(title);
        note.setContent(content);

        return noteRepository.save(note);
    }

    public Note findById(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ServiceException(ExceptionMessages.NOTE_NOT_FOUND.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY));
    }
}
