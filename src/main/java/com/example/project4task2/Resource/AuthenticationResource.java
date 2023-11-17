package com.example.project4task2.Resource;

import com.example.project4task2.Model.User;
import com.example.project4task2.Service.UserService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthenticationResource {

    private UserService userService = new UserService();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        if (user.getUsername() == null || user.getPasswordHash() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username and password are required").build();
        }

        User registeredUser = userService.registerUser(user.getUsername(), user.getPasswordHash());
        if (registeredUser != null) {
            return Response.status(Response.Status.CREATED).entity(registeredUser).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User could not be created").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response loginUser(User user) {
        if (user.getUsername() == null || user.getPasswordHash() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username and password are required").build();
        }

        String token = userService.authenticateUser(user.getUsername(), user.getPasswordHash());
        if (token != null) {
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
        }
    }
}
