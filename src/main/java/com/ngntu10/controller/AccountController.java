package com.ngntu10.controller;

import com.ngntu10.constants.Endpoint;
import com.ngntu10.dto.request.user.UpdatePasswordRequest;
import com.ngntu10.dto.response.SuccessResponse;
import com.ngntu10.dto.response.user.UserResponse;
import com.ngntu10.service.User.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Account.BASE)
@Tag(name = "Account", description = "Account API")
public class AccountController {
    private final UserService userService;

    @GetMapping(Endpoint.Account.ME)
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(UserResponse.convert(userService.getUser()));
    }

    @PostMapping(Endpoint.Account.PASSWORD)
    public ResponseEntity<SuccessResponse> password(
        @Parameter(description = "Request body to update password", required = true)
        @RequestBody @Valid UpdatePasswordRequest request
    ) throws BindException {
        userService.updatePassword(request);

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("Password updated")
            .build());
    }

    @GetMapping(Endpoint.Account.RESEND_EMAIL_VERIFICATION)
    public ResponseEntity<SuccessResponse> resendEmailVerificationMail() {
        userService.resendEmailVerificationMail();

        return ResponseEntity.ok(SuccessResponse.builder()
            .message("Verification email has been sent")
            .build());
    }
}
