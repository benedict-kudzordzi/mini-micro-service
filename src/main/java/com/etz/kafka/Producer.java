package com.etz.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.etz.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Producer {

    @Inject
    @Channel("my-producer")
    Emitter<User> emitter;

    public void sendMessage(User user) {
        emitter.send(user);
    }
}
