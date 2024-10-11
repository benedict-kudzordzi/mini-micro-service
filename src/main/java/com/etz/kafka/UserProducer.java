package com.etz.kafka;

// import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.etz.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserProducer {
    private static final Logger log = Logger.getLogger(UserProducer.class);

    @Inject
    @Channel("my-producer")
    Emitter<User> emitter;

    public void sendMessage(User user) {
        log.info("Incoming data to producer "+user.toString());
        try {
            emitter.send(user).toCompletableFuture().join();
        } catch (Exception e) {
            log.error("Error serializing User to JSON", e);
        }
    }

    // public void sendMessage(User user) {
    //     // Use CompletableFuture to block until the message is sent
    //     CompletableFuture<Void> future = new CompletableFuture<>();

    //     emitter.send(user).whenComplete((result, error) -> {
    //         if (error != null) {
    //             future.completeExceptionally(error);
    //         } else {
    //             log.info("Message Completed");
    //             future.complete(null);
    //         }
    //     });

    //     // Wait for the message to be sent (or handle it in a way that doesn't block indefinitely)
    //     try {
    //         future.join(); // This blocks until the CompletableFuture is completed
    //         log.info("Future Completed");
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to send message to Kafka", e);
    //     }
    // }
}
