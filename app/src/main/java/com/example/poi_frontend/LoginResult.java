package com.example.poi_frontend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.LongToIntFunction;

public class LoginResult {
    private boolean result;
    private int premium;
    private String expiringTime;

    public LoginResult() {
        this.result = false;
    }

    public LoginResult(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        this.result = jsonObject.getBoolean("result");
        this.premium = jsonObject.getInt("premium");
        this.expiringTime = jsonObject.getString("expiring_time");
    }

    public boolean getResult() { return result; }
    public int getPremium() { return premium; }
    public String getExpiringTime() { return expiringTime; }
}
