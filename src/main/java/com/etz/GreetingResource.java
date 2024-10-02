package com.etz;

import java.util.List;

import com.etz.entity.User;
import com.etz.service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mini-micro")
@RequestScoped
@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("/users/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @POST
    @Path("/addUser")
    public Response addUser(User user){
        return userService.addUser(user);
    }

    @GET
    @Path("/user/{id}")
    public Response getUser(@PathParam("id")Long id){
        return userService.getUser(id);
    }

    @PATCH
    @Path("/user/{id}")
    public Response updateUser(@PathParam("id")Long id, User user){
        return userService.updateUser(id, user);
    }

    @PATCH
    @Path("/disableUser/{id}")
    public Response disableUser(@PathParam("id")Long id){
        return userService.disableUser(id);
    }

    @PATCH
    @Path("/enableUser/{id}")
    public Response enableUser(@PathParam("id")Long id){
        return userService.enableUser(id);
    }
}
