package com.example.poi_frontend;

import android.location.GnssAntennaInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Downloader implements Runnable {

    private iListener listener;
    private String ticketCode;

    public Downloader(String ticketCode) { this.ticketCode = ticketCode; }

    public void setListener(iListener listener) { this.listener = listener; }

    @Override
    public void run() {
        try {
            URL url = new URL("http://192.168.4.220/poidb/api/login.php?code=" + this.ticketCode);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String responseText = "";
            String line;
            while((line = bufferedReader.readLine()) != null)
                responseText = responseText.concat(line);

            LoginResult loginResult;
            try {
                loginResult = new LoginResult(responseText);
            } catch (Exception e) {
                loginResult = new LoginResult();
            }
            listener.onTaskComplete(loginResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
