package com.example.poi_frontend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader implements Runnable {

    private iListener listener;
    private int type; // 0 = codice ticket, 1 = codice POI
    private String code;

    public Downloader(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public void setListener(iListener listener) { this.listener = listener; }

    @Override
    public void run() {
        String urlString = (this.type == 0) ? "http://192.168.4.220/poidb/api/login.php?code=" + this.code
                : "http://192.168.4.220/poidb/api/get_poi_data.php?nfccode=" + this.code;

        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String responseText = "";
            String line;

            if (this.type == 0) {
                while ((line = bufferedReader.readLine()) != null)
                    responseText = responseText.concat(line);

                LoginResult loginResult;
                try {
                    loginResult = new LoginResult(responseText);
                } catch (Exception e) {
                    loginResult = new LoginResult();
                }
                listener.onTaskComplete(loginResult);
            }
            else {
                while ((line = bufferedReader.readLine()) != null)
                    responseText = responseText.concat(line);

                if (listener != null)
                    listener.onTaskComplete(responseText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
