package com.etz.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import org.jboss.logging.Logger;

import com.etz.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserConsumer {
    private static final Logger log = Logger.getLogger(UserConsumer.class);

    @Inject
    @Channel("status-out")
    Emitter<User> emitter;

    // @Incoming("my-queue")
    // public void consume(User user) {
    //     log.info("Received message: " + user);
    // }

    @Incoming("my-queue")
    public String process(User message){
        log.info("Incoming message "+message);
        emitter.send(message);

        return "Message Sent !!";
    }

    // @Incoming("status-in")
    // public void processStatus(String messsage){
    //     log.info("Status Message "+messsage);
    // }
}
