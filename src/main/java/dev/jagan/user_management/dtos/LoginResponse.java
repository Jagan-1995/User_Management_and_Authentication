package dev.jagan.user_management.dtos;




public class LoginResponse {
    private String token;

    // Constructor accepting a String
    public LoginResponse(String token) {
        this.token = token;
    }

    // Default no-args constructor (if needed)
    public LoginResponse() {
    }

    // Getter and Setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
