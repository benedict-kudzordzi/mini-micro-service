package com.etz.service;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.etz.entity.User;

import com.etz.kafka.UserProducer;
import com.etz.notification.NotificationService;
import com.etz.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    @Inject
    UserProducer producer;

    @Inject
    NotificationService notificationService;

    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    public Response addUser(User user) {
        user.setStatus("1");

        userRepository.persist(user);

        // send message to kafka
        producer.sendMessage(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @Incoming("status-in")
    @Transactional
    public void processStatus(User user) {
        log.info("Incoming user status data " + user.toString());

        String notificationResponse = notificationService.getRandomResponse(user);
        log.info("Notification Response "+notificationResponse);
        JSONObject response = new JSONObject(notificationResponse);

        String status = response.getString("status");
        Long userId = response.getLong("userId");

        userRepository.findByIdOptional(userId)
        .map(existingUser -> {
            existingUser.setNotificationStatus(status);
            userRepository.persist(existingUser);
            log.info("User notification status updated successfully");
            return existingUser;
        })
        .orElseGet(() -> {
            log.infof("User with ID %s does not exist", userId);
            return null; 
        });

        // Optional<User> optUser = userRepository.findByIdOptional(userId);

        // if (optUser.isPresent()) {
        //     User existingUser = optUser.get();

        //     existingUser.setNotificationStatus(status);

        //     userRepository.persist(existingUser);
        //     log.info("User notification status updated successfully");
        // } else {
        //     log.infof("User with ID %s does not exist", userId);
        // }

    }

    public Response getUser(Long id) {
        Optional<User> user = userRepository.findByIdOptional(id);

        // return userRepository.find("id", id ).singleResultOptional().map(user ->
        // Response.status(Response.Status.OK).entity(user)).orElse(Response.status(Response.Status.NOT_FOUND).entity("User
        // Not Found").build());

        if (user.isPresent()) {
            User checkUserStatus = user.get();

            if (checkUserStatus.getStatus().equals("0")) {
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

    public Response disableUser(Long id) {
        Optional<User> optionalUser = userRepository.findByIdOptional(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setStatus("0");

            userRepository.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }

    public Response enableUser(Long id) {
        Optional<User> optionalUser = userRepository.findByIdOptional(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setStatus("1");

            userRepository.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }

    }
}
