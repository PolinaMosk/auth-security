package org.inform.security.rest;

import org.inform.security.converter.UserEntityToDTOConverter;
import org.inform.security.dto.CredentialsDTO;
import org.inform.security.entity.Credentials;
import org.inform.security.exceptions.InvalidPasswordException;
import org.inform.security.exceptions.UserNotFoundException;
import org.inform.security.repository.CredentialsRepository;
import org.inform.security.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder encoder;
    private final UserEntityToDTOConverter userEntityToDTOConverter;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(PasswordEncoder encoder, CredentialsRepository credentialsRepository, UserEntityToDTOConverter userEntityToDTOConverter) {
        this.credentialsRepository = credentialsRepository;
        this.encoder = encoder;
        this.userEntityToDTOConverter = userEntityToDTOConverter;
    }

    public String signIn(CredentialsDTO credentials) {
        if (checkCredentials(credentials)) {
            return JWTUtil.createToken(credentials.getLogin());
        } else {
            throw new InvalidPasswordException("Invalid password");
        }
    }

    private boolean checkCredentials(CredentialsDTO credentials) {
        Optional<Credentials> userCredentials = credentialsRepository.findByLogin(credentials.getLogin());
        if (userCredentials.isEmpty()) {
            throw new UserNotFoundException("User doest not exist");
        }
        return encoder.matches(credentials.getPassword(), userCredentials.get().getPasswordHash());
    }
}
