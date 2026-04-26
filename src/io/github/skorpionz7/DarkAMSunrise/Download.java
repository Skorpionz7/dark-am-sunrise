package io.github.skorpionz7.darkamsunrise;

public class Download {
    private String username;
    Download() {
        username = "Skorpion";
    }
    public synchronized String getUsername() {
        return username;
    }
    public synchronized void setUsername(String name) {
        username = name;
    }
}
