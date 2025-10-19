package api.example.SimpleNotes.domain.user;

import api.example.SimpleNotes.domain.user.dto.request.UserRequest;
import api.example.SimpleNotes.domain.user.dto.response.LoginResponseUser;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;
import api.example.SimpleNotes.domain.user_token.TokenType;
import api.example.SimpleNotes.domain.user_token.UserTokenService;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.email.dto.ConfirmEmailEvent;
import api.example.SimpleNotes.infrastructure.email.dto.ForgotPasswordEvent;
import api.example.SimpleNotes.infrastructure.security.AuditorAwareImpl;
import api.example.SimpleNotes.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import static api.example.SimpleNotes.infrastructure.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final UserTokenService userTokenService;
    private final ApplicationEventPublisher eventPublisher;
    private final AuditorAwareImpl auditorAware;

    @Transactional
    public void register(String email, String name, String password) {

        this.validateRegistrationData(email, name);

        String passwordEncoder = encoder.encode(password);

        User user = new User(email, name, passwordEncoder);

        User savedUser = repository.save(user);

        this.saveUserTokenAndSendEmail(savedUser);
    }
    
    public LoginResponseUser login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        if(user.isAccountNonLocked()) {
            this.saveUserTokenAndSendEmail(user);
        }

        if(!encoder.matches(password, user.getPassword())) {
            throw new ServiceException(INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        String generatedToken = tokenService.generateToken(user);

        return new LoginResponseUser(user, generatedToken);
    }

    @Transactional
    public void confirmEmail(String token) {
        User user = userTokenService.validateAndUseToken(token);

        user.setAccountNonLocked(true);
        user.setCreatedBy(user.getEmail());

        repository.save(user);
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        String rawToken = UUID.randomUUID().toString();

        userTokenService.saveUserToken(user, rawToken, TokenType.EMAIL_VERIFICATION_TOKEN);

        eventPublisher.publishEvent(new ForgotPasswordEvent(user.getEmail(), rawToken, user.getName()));
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userTokenService.validateAndUseToken(token);

        String passwordEncoder = encoder.encode(newPassword);

        user.setPassword(passwordEncoder);

        repository.save(user);
    }

    @Transactional(readOnly = true)
    public PageDTO<UserResponse> findAll(Pageable pageable) {
        Page<User> users = repository.findAllByIsAccountNonLockedIsTrue(pageable);

        Page<UserResponse> dtosPage = users.map(UserResponse::new);

        return new PageDTO<>(
                dtosPage.getContent(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages());
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        return repository.findByIdAndCreatedByAndIsAccountNonLockedIsTrue(id, currentAuditor.get())
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = this.findById(id);

        if (!user.getEmail().equals(userRequest.email())) {
            validadeIfEmailExists(userRequest.email());
            user.setEmail(userRequest.email());
            user.setAccountNonLocked(false);
            this.saveUserTokenAndSendEmail(user);
        }

        if (!user.getName().equals(userRequest.name())) {
            validadeIfNameExists(userRequest.name());
            user.setName(userRequest.name());
        }

        return user;
    }

    private void validateRegistrationData (String email, String username) {
        validadeIfEmailExists(email);
        validadeIfNameExists(username);
    }

    private void validadeIfEmailExists(String email) {
        if (repository.existsByEmail(email)) {
            throw new ServiceException(EMAIL_IN_USE.getMessage(), HttpStatus.CONFLICT);
        }
    }

    private void validadeIfNameExists(String username) {
        if (repository.existsByName(username)) {
            throw new ServiceException(NAME_IN_USE.getMessage(), HttpStatus.CONFLICT);

        }
    }

    private void saveUserTokenAndSendEmail(User user) {
        String rawToken = UUID.randomUUID().toString();

        userTokenService.saveUserToken(user, rawToken, TokenType.EMAIL_VERIFICATION_TOKEN);

        eventPublisher.publishEvent(new ConfirmEmailEvent(user.getEmail(), rawToken, user.getName()));
    }
}
