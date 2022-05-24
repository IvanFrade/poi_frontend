package com.example.poi_frontend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media {
    private int id;
    private String poi;
    private String label;
    private String url;
    private String type;
    private String language;

    public Media(JSONObject media) throws JSONException {
        this.id = media.getInt("id");
        this.poi = media.getString("poi");
        this.label = media.getString("label");
        this.url = media.getString("url");
        this.type = media.getString("type");
        this.language = media.getString("language");
    }

    public String getLabel() { return this.label; }
    public String getUrl() { return this.url; }
}
