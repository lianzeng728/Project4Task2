package com.example.project4task2.Servlet;

import com.example.project4task2.Model.User;
import com.example.project4task2.Service.UserService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServlet extends HttpServlet {
    private final static Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if ("/register".equals(path)) {
            handleRegister(request, response);
        } else if ("/login".equals(path)) {
            handleLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        User user = gson.fromJson(reader, User.class);

        if (user.getUsername() == null || user.getPasswordHash() == null) {
            LOGGER.log(Level.WARNING, "Username and password are required for registration.");
            System.out.println("Username and password are required for registration.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and password are required");
            return;
        }

        LOGGER.log(Level.INFO, "Attempting to register user: {0}", user.getUsername());
        User registeredUser = userService.registerUser(user.getUsername(), user.getPasswordHash());

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (registeredUser != null) {
            LOGGER.log(Level.INFO, "User registered successfully: " + user.getUsername());
            System.out.println("User registered successfully: " + user.getUsername());
            out.print(gson.toJson(registeredUser));
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            LOGGER.log(Level.SEVERE, "User registration failed for: " + user.getUsername());
            System.out.println("User registration failed for: " + user.getUsername());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.flush();
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        User user = gson.fromJson(reader, User.class);

        if (user.getUsername() == null || user.getPasswordHash() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and password are required");
            return;
        }

        LOGGER.log(Level.INFO, "Attempting to authenticate user: {0}", user.getUsername());
        String token = userService.authenticateUser(user.getUsername(), user.getPasswordHash());

        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        if (token != null) {
            LOGGER.log(Level.INFO, "User authenticated: {0}", user.getUsername());
            out.print(token);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            LOGGER.log(Level.WARNING, "Invalid login attempt for user: {0}", user.getUsername());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        out.flush();
    }
}