package com.etz.service;

import java.util.List;
import java.util.Optional;

import com.etz.entity.User;
import com.etz.kafka.Producer;
import com.etz.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    Producer producer;

    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    
    public Response addUser(User user) {
        user.setStatus("1");
        producer.sendMessage(user);

        userRepository.persist(user);

       
        // kafka

        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    public Response getUser(Long id) {
        Optional<User> user = userRepository.findByIdOptional(id);

        // return userRepository.find("id", id ).singleResultOptional().map(user -> Response.status(Response.Status.OK).entity(user)).orElse(Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build());

        if (user.isPresent()) {
            User checkUserStatus = user.get();

            if(checkUserStatus.getStatus().equals("0")){
                return Response.status(Response.Status.OK).entity("User has been disabled").build();
            }
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }

    public Response updateUser(Long id, User user) {
        Optional<User> optUser = userRepository.findByIdOptional(id);

        if (optUser.isPresent()) {
            User existingUser = optUser.get();

            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            userRepository.persist(existingUser);

            return Response.status(Response.Status.OK).entity(existingUser).build();

        } else {

            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }

    public Response disableUser(Long id){
        Optional<User> optionalUser = userRepository.findByIdOptional(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            user.setStatus("0");

            userRepository.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }

    public Response enableUser(Long id){
        Optional<User> optionalUser = userRepository.findByIdOptional(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            user.setStatus("1");

            userRepository.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }
}
