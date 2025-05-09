package com.ngntu10.controller;

import com.ngntu10.constants.Endpoint;
import com.ngntu10.dto.request.auth.LoginRequest;
import com.ngntu10.dto.request.auth.PasswordRequest;
import com.ngntu10.dto.request.auth.RegisterRequest;
import com.ngntu10.dto.request.auth.ResetPasswordRequest;
import com.ngntu10.dto.response.APIResponse;
import com.ngntu10.dto.response.ErrorResponse;
import com.ngntu10.dto.response.SuccessResponse;
import com.ngntu10.dto.response.auth.PasswordResetResponse;
import com.ngntu10.dto.response.auth.TokenResponse;
import com.ngntu10.dto.response.user.UserResponse;
import com.ngntu10.entity.User;
import com.ngntu10.exception.NotFoundException;
import com.ngntu10.repository.UserRepository;
import com.ngntu10.service.Auth.AuthService;
import com.ngntu10.service.Token.PasswordResetTokenService;
import com.ngntu10.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ngntu10.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequestMapping(Endpoint.Auth.BASE)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth")
public class AuthController {
    private final AuthService authService;
    
    private final UserService userService;

    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRepository userRepository;

    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<APIResponse<TokenResponse>> login(
        @Parameter(description = "Request body to login", required = true)
        @RequestBody @Validated final LoginRequest loginRequest
    ) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(new APIResponse<TokenResponse>(false, 200, tokenResponse, "Login successful"));
    }

    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<APIResponse<UserResponse>> register(
        @Parameter(description = "Request body to register", required = true)
        @RequestBody @Valid RegisterRequest request
    ) throws BindException {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(new APIResponse<UserResponse>(false, 200, user, "Registered successfully, please check your mail for verification")
        );
    }

    @GetMapping(Endpoint.Auth.EMAIL_VERIFICATION_TOKEN)
    public ResponseEntity<SuccessResponse> emailVerification(
        @Parameter(name = "token", description = "E-mail verification token", required = true)
        @PathVariable("token") final String token
    ) {
        userService.verifyEmail(token);

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("Your email verified")
            .build());
    }

    @GetMapping(Endpoint.Auth.RESET_PASSWORD_EMAIL)
    public ResponseEntity<SuccessResponse> resetPassword(
            @Parameter(name = "email", description = "Password reset email", required = true)
            @PathVariable("email") final String email
    ) {
        authService.resetPassword(email);

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("password reset link sent")
            .build());
    }

//    @GetMapping(Endpoint.Auth.RESET_PASSWORD_EMAIL)
//    public ResponseEntity<String> resetPassword(
//        @Parameter(name = "email", description = "Password reset email", required = true)
//        @PathVariable("email") final String email
//    ) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//        userService.emailVerificationEventPublisher(user);
//        return ResponseEntity.ok("OTP send");
//    }

    @PostMapping(Endpoint.Auth.RESET_PASSWORD_OTP)
    public ResponseEntity<SuccessResponse> resetPassword(
        @Parameter(name = "otp", description = "otp password reset", required = true)
        @PathVariable("otp")  String otp,
        @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(otp, request);

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("Password reset success successfully")
            .build());
    }

    @GetMapping(Endpoint.Auth.LOGOUT)
    public ResponseEntity<SuccessResponse> logout() {
//        authService.logout(userService.getUser());

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("Logout successfully")
            .build());
    }
}
