package application.exceptions;

public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse(){
        this.username = "Неверный логин или пароль";
        this.password = "Неверный логин или пароль";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
