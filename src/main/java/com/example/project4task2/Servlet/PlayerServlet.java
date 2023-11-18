package com.example.project4task2.Servlet;

import com.example.project4task2.Model.Player;
import com.example.project4task2.Service.PlayerService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerServlet extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(PlayerServlet.class.getName());
    private PlayerService playerService = new PlayerService();
    private Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Search term is required");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search term is required");
            return;
        }

        LOGGER.log(Level.INFO, "Fetching players with search term: {0}", searchTerm);
        List<Player> players = playerService.fetchPlayers(searchTerm);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(gson.toJson(players));
        out.flush();

        LOGGER.log(Level.INFO, "Players fetched successfully for term: {0}", searchTerm);
    }
}