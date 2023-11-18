package com.example.project4task2.Service;

import com.example.project4task2.Model.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PlayerService {

    private final static Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    public List<Player> fetchPlayers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search term is invalid.");
            return new ArrayList<>();
        }

        LOGGER.log(Level.INFO, "Fetching players with search term: {0}", searchTerm);
        System.out.println("Fetching players from the NBA API for term: " + searchTerm);

        List<Player> players = new ArrayList<>();

        try {
            JSONObject jsonResponse = getRemoteJSON("https://free-nba.p.rapidapi.com/players?per_page=100&search=" + searchTerm);

            if (jsonResponse == null) {
                LOGGER.log(Level.WARNING, "NBA API is unavailable.");
                return players;
            }

            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            if (jsonArray == null) {
                LOGGER.log(Level.WARNING, "Invalid data received from NBA API.");
                return players;
            }

            // Inside your loop in the fetchPlayers method
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPlayer = jsonArray.getJSONObject(i);
                Player player = new Player(
                        jsonPlayer.getString("id"),
                        jsonPlayer.getString("first_name"),
                        jsonPlayer.getString("last_name"),
                        jsonPlayer.optString("position", "N/A"),
                        jsonPlayer.optJSONObject("team").optString("full_name", "Unknown Team")
                );
                players.add(player);
            }
            LOGGER.log(Level.INFO, "Players fetched successfully");
            System.out.println("Player data fetched successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching players: {0}", e.getMessage());
            System.out.println("Error fetching player data: " + e.getMessage());
        }
        return players;
    }

    private JSONObject getRemoteJSON(String url) {
        try {
            String api_key = "YOUR_API_KEY";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-RapidAPI-Key", api_key);
            connection.setRequestProperty("X-RapidAPI-Host", "free-nba.p.rapidapi.com");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting remote JSON: {0}", e.getMessage());
            System.out.println("Error getting remote JSON: " + e.getMessage());
            return null;
        }
    }
}