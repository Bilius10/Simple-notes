package api.example.SimpleNotes.domain.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Transactional(readOnly = true)
    Page<Note> findAllByWalletId(Long walletId, Pageable pageable);
}
