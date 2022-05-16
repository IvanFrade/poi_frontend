package com.example.poi_frontend;

import android.location.GnssAntennaInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Downloader implements Runnable {

    public iListener listener;

    public void setListener(iListener listener) { this.listener = listener; }

    @Override
    public void run() {
        try {
            URL url = new URL("http://192.168.4.220/poidb/api/login.php?code=bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String responseText = "";
            String line;
            while((line = bufferedReader.readLine()) != null)
                responseText = responseText.concat(line);

            listener.onTaskComplete(responseText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
