package entities.user;

import java.util.UUID;

public class User {

    private final UUID id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        setUsername(username);
        setPassword(password);
    }

    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    void setUsername(String username) {
        UserValidator.validateUsername(username);
        this.username = username;
    }

    void setPassword(String password) {
        UserValidator.validatePassword(password);
        this.password = password;
    }

}