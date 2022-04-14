package org.inform.security.rest;

import org.inform.security.dto.CredentialsDTO;
import org.inform.security.exceptions.InvalidJwtAuthenticationException;
import org.inform.security.exceptions.InvalidPasswordException;
import org.inform.security.exceptions.JWTValidityExpiredException;
import org.inform.security.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/v1/secure")
    public ResponseEntity<?> secure() {
        return ResponseEntity.status(HttpStatus.OK).body("You have accessed endpoint through access-token");
    }

    @PostMapping("/api/v1/signin")
    public ResponseEntity<?> signIn(@RequestBody CredentialsDTO credentials) {
        String jwtToken = userService.signIn(credentials);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<String> handleJWTEx() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT-token does not match locally created one");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordEx() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong or invalid password");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundEx() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong or invalid login");
    }

    @ExceptionHandler(JWTValidityExpiredException.class)
    public ResponseEntity<String> handleJWTValidityEx() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT-token expired");
    }
}
