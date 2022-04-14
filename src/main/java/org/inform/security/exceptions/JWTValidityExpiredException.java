package org.inform.security.exceptions;

public class JWTValidityExpiredException extends RuntimeException{
    public JWTValidityExpiredException(String message) {
        super(message);
    }
}
