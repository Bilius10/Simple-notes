package api.example.SimpleNotes.domain.note;

import api.example.SimpleNotes.domain.note.dto.response.NoteResponse;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;
import api.example.SimpleNotes.domain.wallet.Wallet;
import api.example.SimpleNotes.domain.wallet.WalletService;
import api.example.SimpleNotes.domain.wallet_user.WalletPermission;
import api.example.SimpleNotes.domain.wallet_user.WalletUserService;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        walletUserService.verifyHasPermisson(walletId, userId, WalletPermission.CREATE);

        Note note = new Note(title, content, wallet);

        return noteRepository.save(note);
    }

    public PageDTO<NoteResponse> findAll(Long walletId, Long userId, Pageable pageable) {
        walletService.findById(walletId, userId);

        walletUserService.verifyHasPermisson(walletId, userId, WalletPermission.VIEW);

        Page<Note> notes = noteRepository.findAllByWalletId(walletId, pageable);

        Page<NoteResponse> dtosPage = notes.map(NoteResponse::new);

        return new PageDTO<>(
                dtosPage.getContent(),
                notes.getNumber(),
                notes.getSize(),
                notes.getTotalElements(),
                notes.getTotalPages());

    }
}
