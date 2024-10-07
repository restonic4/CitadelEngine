package com.restonic4.citadel.util;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubHelper {
    public static String getLatestRelease(String owner, String repo) {
        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());

                String latestVersion = json.getString("tag_name");
                return latestVersion;
            } else {
                throw new RuntimeException("Error: " + response.statusCode());
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
