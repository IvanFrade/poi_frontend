package com.example.poi_frontend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class POI {
    private int id;
    private String name;
    private String nfccode;
    private Media[] media;

    public POI(JSONObject poi) throws JSONException {
        this.id = poi.getInt("id");
        this.name = poi.getString("name");
        this.nfccode = poi.getString("nfccode");


        JSONArray mediaArray = poi.getJSONArray("media");
        media = new Media[mediaArray.length()];
        for(int i = 0; i < mediaArray.length(); i++) {
            media[i] = new Media(mediaArray.getJSONObject(i));
        }
    }

    public String getName() { return this.name; }
    public Media[] getMedia() { return this.media; }

}
