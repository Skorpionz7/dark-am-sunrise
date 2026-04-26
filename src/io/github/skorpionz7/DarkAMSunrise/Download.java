package io.github.skorpionz7.darkamsunrise;

public class Download {
    private final String username;
    Download() {
        username = "Skorpion";
    }
    public synchronized String getUsername() {
        return username;
    }
}
