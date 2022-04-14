package org.inform.security.converter;

import org.inform.security.dto.UserDTO;
import org.inform.security.entity.User;
import org.inform.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDTOToEntityConverter implements Converter<UserDTO, User> {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserDTOToEntityConverter.class);

    public UserDTOToEntityConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User convert(UserDTO userDTO) {
        logger.info(userDTO.toString());
        if (userDTO.getId() != null) {
            Optional<User> user = userRepository.findById(userDTO.getId());
            return user.orElse(null);
        } else {
            return new User(userDTO.getFirstName(), userDTO.getLastName());
        }
    }
}
