package com.example.poi_frontend;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class POIDownloader implements Runnable {
    private String codicePOI;
    private iListener listener;

    public POIDownloader(String codice) {
        this.codicePOI = codice;
    }

    public void setListener(iListener listener) { this.listener = listener; }

    @Override
    public void run() {
        try {
            URL url = new URL("http://192.168.4.220/poidb/api/get_poi_data.php?nfccode=" + codicePOI);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String responseText = "";
            String line;

            while ((line = bufferedReader.readLine()) != null)
                responseText = responseText.concat(line);

            if (listener != null)
                listener.onTaskComplete(responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
