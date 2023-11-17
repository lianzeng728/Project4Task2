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
        LOGGER.log(Level.INFO, "Fetching players with search term: {0}", searchTerm);
        System.out.println("Fetching players from the NBA API for term: " + searchTerm);
        List<Player> players = new ArrayList<>();
        try {
            JSONObject jsonResponse = getRemoteJSON("https://free-nba.p.rapidapi.com/players?per_page=100&search=" + searchTerm);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            // Inside your loop in the fetchPlayers method
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPlayer = jsonArray.getJSONObject(i);
                String id = jsonPlayer.getString("id");
                String firstName = jsonPlayer.getString("first_name");
                String lastName = jsonPlayer.getString("last_name");
                String position = jsonPlayer.optString("position", "N/A");
                JSONObject teamObj = jsonPlayer.optJSONObject("team");
                String team = teamObj != null ? teamObj.getString("full_name") : "Unknown Team";

                Player player = new Player(id, firstName, lastName, position, team);
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