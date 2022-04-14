package org.inform.security.security.userdetails;

import org.inform.security.entity.Credentials;
import org.inform.security.entity.User;
import org.inform.security.repository.CredentialsRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final CredentialsRepository credentialRepository;
    public CustomUserDetailsService(CredentialsRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }
    @Override
    public CustomUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Credentials> credentials = credentialRepository.findByLogin(login);
        if (credentials.isPresent()) {
            CustomUserDetails userDetails = new CustomUserDetails();
            userDetails.setLogin(credentials.get().getLogin());
            userDetails.setPassword(credentials.get().getPasswordHash());
            return userDetails;
        } else {
            return null;
        }
    }
}
