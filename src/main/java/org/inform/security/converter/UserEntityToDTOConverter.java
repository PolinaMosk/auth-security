package org.inform.security.converter;

import org.inform.security.dto.UserDTO;
import org.inform.security.entity.Credentials;
import org.inform.security.entity.User;
import org.inform.security.repository.CredentialsRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOConverter implements Converter<User, UserDTO> {
    private final CredentialsRepository credentialsRepository;

    public UserEntityToDTOConverter(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }
    @Override
    public UserDTO convert(User userEntity) {
        Credentials credentials = getCredentials(userEntity.getId());
        return new UserDTO(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), credentials.getLogin());
    }

    private Credentials getCredentials(Long userId) {
        return credentialsRepository.findByUserId(userId).orElse(null);
    }
}
