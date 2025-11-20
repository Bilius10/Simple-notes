package api.example.SimpleNotes.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);

    @Transactional(readOnly = true)
    Page<User> findAllByIsAccountNonLockedIsTrueAndIdNot(Pageable pageable, Long id);

    @Transactional(readOnly = true)
    Optional<User> findByIdAndIsAccountNonLockedIsTrue(Long id);

    boolean existsByEmail(String email);

    boolean existsByName(String username);

}
