package org.inform.security.dto;

public class CredentialsDTO {
    private Long id;
    private Long userId;
    private String login;
    private String password;

    public CredentialsDTO() {
    }

    public CredentialsDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CredentialsDTO(Long id, Long userId, String login, String password) {
        this.id = id;
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
