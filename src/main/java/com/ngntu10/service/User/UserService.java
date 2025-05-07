package com.ngntu10.service.User;

import com.ngntu10.dto.request.auth.RegisterRequest;
import com.ngntu10.dto.request.auth.ResetPasswordRequest;
import com.ngntu10.dto.request.user.*;
import com.ngntu10.dto.response.user.UserResponse;
import com.ngntu10.entity.User;
import com.ngntu10.service.Token.EmailVerificationTokenService;
import com.ngntu10.service.Token.PasswordResetTokenService;
import com.ngntu10.service.Role.RoleService;
import com.ngntu10.specification.UserFilterSpecification;
import com.ngntu10.specification.criteria.PaginationCriteria;
import com.ngntu10.specification.criteria.UserCriteria;
import com.ngntu10.event.UserEmailVerificationSendEvent;
import com.ngntu10.event.UserPasswordResetSendEvent;
import com.ngntu10.exception.BadRequestException;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.UserRepository;
import com.ngntu10.security.JwtUserDetails;
import com.ngntu10.util.Constants;
import com.ngntu10.util.PageRequestBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final EmailVerificationTokenService emailVerificationTokenService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Get authentication.
     *
     * @return Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Return the authenticated user.
     *
     * @return user User
     */
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                return findById(getPrincipal(authentication).getId());
            } catch (ClassCastException | NotFoundException e) {
                log.warn("[JWT] User details not found!");
                throw new BadCredentialsException("bad credentials");
            }
        } else {
            log.warn("[JWT] User not authenticated!");
            throw new BadCredentialsException("bad credentials");
        }
    }

    /**
     * Count users.
     *
     * @return Long
     */
    public long count() {
        return userRepository.count();
    }

    /**
     * Find all users with pagination.
     *
     * @param criteria           UserCriteria
     * @param paginationCriteria PaginationCriteria
     * @return Page
     */
    public Page<User> findAll(UserCriteria criteria, PaginationCriteria paginationCriteria) {
        return userRepository.findAll(new UserFilterSpecification(criteria),
            PageRequestBuilder.build(paginationCriteria));
    }

    /**
     * Find a user by id.
     *
     * @param id UUID
     * @return User
     */
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));
    }

    /**
     * Find a user by id.
     *
     * @param id String
     * @return User
     */
    public User findById(String id) {
        return findById(UUID.fromString(id));
    }

    /**
     * Find a user by email.
     *
     * @param email String.
     * @return User
     */
    public User findByEmail(final String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));
    }

    /**
     * Load user details by username.
     *
     * @param email String
     * @return UserDetails
     * @throws UsernameNotFoundException email not found exception.
     */
    public UserDetails loadUserByEmail(final String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));

        return JwtUserDetails.create(user);
    }

    /**
     * Loads user details by UUID string.
     *
     * @param id String
     * @return UserDetails
     */
    public UserDetails loadUserById(final String id) {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));

        return JwtUserDetails.create(user);
    }

    /**
     * Get UserDetails from security context.
     *
     * @param authentication Wrapper for security context
     * @return the Principal being authenticated or the authenticated principal after authentication.
     */
    public JwtUserDetails getPrincipal(final Authentication authentication) {
        return (JwtUserDetails) authentication.getPrincipal();
    }

    /**
     * Register user.
     *
     * @param request RegisterRequest
     * @return User
     */
    public UserResponse register(final RegisterRequest request) throws BindException {
        log.info("Registering user with email: {}", request.getEmail());

        User user = createUser(request);
        user.setRoles(List.of(roleService.findByName(Constants.RoleEnum.USER)));
        userRepository.save(user);

        emailVerificationEventPublisher(user);

        log.info("User registered with email: {}, {}", user.getEmail(), user.getId());

        UserResponse userResponse = UserResponse.convert(user);
        return userResponse;
    }

    /**
     * Create user.
     *
     * @param request CreateUserRequest
     * @return User
     */
    public User create(final CreateUserRequest request) throws BindException {
        log.info("Creating user with email: {}", request.getEmail());

        User user = createUser(request);
        request.getRoles().forEach(role -> user.getRoles()
            .add(roleService.findByName(Constants.RoleEnum.get(role))));

        if (request.getIsEmailVerified() != null && request.getIsEmailVerified()) {
            user.setEmailVerifiedAt(LocalDateTime.now());
        }

        if (request.getIsBlocked() != null && request.getIsBlocked()) {
            user.setBlockedAt(LocalDateTime.now());
        }

        userRepository.save(user);

        log.info("User created with email: {}, {}", user.getEmail(), user.getId());

        return user;
    }

    /**
     * Update user.
     *
     * @param id      UUID
     * @param request UpdateUserRequest
     * @return User
     */
    public User update(UUID id, UpdateUserRequest request) throws BindException {
        User user = findById(id);
        user.setEmail(request.getEmail());
        

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null) {
            user.setRoles(request.getRoles().stream()
                .map(role -> roleService.findByName(Constants.RoleEnum.get(role)))
                .collect(Collectors.toList()));
        }

        if (request.getIsEmailVerified() != null) {
            if (request.getIsEmailVerified()) {
                user.setEmailVerifiedAt(LocalDateTime.now());
            } else {
                user.setEmailVerifiedAt(null);
            }
        }

        return updateUser(user, request);
    }

    /**
     * Update user.
     *
     * @param id      String
     * @param request UpdateUserRequest
     * @return User
     */
    public User update(String id, UpdateUserRequest request) throws BindException {
        return update(UUID.fromString(id), request);
    }

    /**
     * Update user password.
     *
     * @param request UpdatePasswordRequest
     */
    public User updatePassword(UpdatePasswordRequest request) throws BindException {
        User user = getUser();
        log.info("Updating password for user with email: {}", user.getEmail());

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "oldPassword",
               "invalid_old_password"));
        }

        if (request.getOldPassword().equals(request.getPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password",
                "same_password_error"));
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("Password updated for user with email: {}", user.getEmail());

        return user;
    }

    /**
     * Reset password.
     *
     * @param token String
     * @param request ResetPasswordRequest
     */
    public void resetPassword(String token, ResetPasswordRequest request) {
        User user = passwordResetTokenService.getUserByToken(token);
        log.info("Resetting password for user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        passwordResetTokenService.deleteByUserId(user.getId());
        log.info("Password reset for user with email: {}", user.getEmail());
    }

    /**
     * Resend e-mail verification mail.
     */
    public void resendEmailVerificationMail() {
        User user = getUser();
        log.info("Resending e-mail verification mail to email: {}", user.getEmail());
        if (user.getEmailVerifiedAt() != null) {
            throw new BadRequestException("your_email_already_verified");
        }

        emailVerificationEventPublisher(user);
        log.info("Resending e-mail verification mail to email: {}", user.getEmail());
    }

    /**
     * E-mail verification.
     *
     * @param token String
     */
    public void verifyEmail(String token) {
        log.info("Verifying e-mail with token: {}", token);
        User user = emailVerificationTokenService.getUserByToken(token);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        emailVerificationTokenService.deleteByUserId(user.getId());
        log.info("E-mail verified with token: {}", token);
    }

    /**
     * Send password reset mail.
     *
     * @param email String
     */
    public void sendEmailPasswordResetMail(String email) {
        log.info("Sending password reset mail to email: {}", email);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not_found_with_param"));

        passwordResetEventPublisher(user);
        log.info("Password reset mail sent to email: {}", email);
    }

    /**
     * Delete user.
     *
     * @param id UUID
     */
    public void delete(String id) {
        userRepository.delete(findById(id));
    }

    /**
     * Create user.
     *
     * @param request AbstractBaseCreateUserRequest
     * @return User
     */
    private User createUser(AbstractBaseCreateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        userRepository.findByEmail(request.getEmail())
            .ifPresent(user -> {
                log.error("User with email: {} already exists", request.getEmail());
                bindingResult.addError(new FieldError(bindingResult.getObjectName(), "email",
                    "unique email"));
            });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .company(request.getCompany())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    /**
     * Update user.
     *
     * @param user    User
     * @param request UpdateUserRequest
     * @return User
     */
    private User updateUser(User user, AbstractBaseUpdateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!user.getEmail().equals(request.getEmail()) &&
            userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "email",
                "already_exists"));
        }

        boolean isRequiredEmailVerification = false;
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
            user.setEmailVerificationToken(emailVerificationTokenService.create(user));
            isRequiredEmailVerification = true;
        }

        if (StringUtils.hasText(request.getFullName()) && !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        userRepository.save(user);

        if (isRequiredEmailVerification) {
            emailVerificationEventPublisher(user);
        }

        return user;
    }

    /**
     * E-mail verification event publisher.
     *
     * @param user User
     */
    protected void emailVerificationEventPublisher(User user) {
        user.setEmailVerificationToken(emailVerificationTokenService.create(user));
        eventPublisher.publishEvent(new UserEmailVerificationSendEvent(this, user));
    }

    /**
     * Password reset event publisher.
     *
     * @param user User
     */
    private void passwordResetEventPublisher(User user) {
        user.setPasswordResetToken(passwordResetTokenService.create(user));
        eventPublisher.publishEvent(new UserPasswordResetSendEvent(this, user));
    }
}
