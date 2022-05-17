package com.example.poi_frontend;

public interface iListener {
    void onTaskComplete(Boolean isValid);
    void onTaskComplete(LoginResult loginResult);
}
