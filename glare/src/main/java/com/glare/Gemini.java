package com.glare;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Gemini {
    
    private final String API_KEY = "******";
    private final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public String getResponse(String message) throws Exception{

        JSONObject json = new JSONObject();
    
        json.put("contents", new JSONArray().put(new JSONObject().put("parts", new JSONArray().put(new JSONObject().put("text", message)))));



        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(URL))
            .header("Content-Type", "application/json;charset=UTF-8")
            .POST(BodyPublishers.ofString(json.toString()))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        JSONObject parsedResponse = new JSONObject(response.body());

        return parsedResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");
    }

}
