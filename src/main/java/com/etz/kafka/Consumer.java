package com.etz.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import org.jboss.logging.Logger;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Consumer {
    private static final Logger log = Logger.getLogger(Consumer.class);

    @Inject
    @Channel("my-producer")
    Emitter<String> emitter;

    // @Incoming("my-queue")
    // public void consume(User user) {
    //     log.info("Received message: " + user);
    // }

    @Incoming("my-queue")
    public String process(String message){
        log.info("Incoming message "+message);
        emitter.send(message);

        return message;
    }
}
