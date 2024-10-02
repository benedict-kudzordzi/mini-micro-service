package com.etz.notification;

import java.util.Random;

import org.json.JSONObject;

import com.etz.kafka.Consumer;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationService {
    private static final Random rand = new Random();

    @Inject
    Consumer consumer;

    @GET
    public Response getRandomResponse(String input) {
        JSONObject response = new JSONObject();

        int randomValue = rand.nextInt(3);

        String responseMessage;
        switch (randomValue) {
            case 0:
                responseMessage = "success";
                break;
            case 1:
                responseMessage = "failed";
                break;
            case 2:
                responseMessage = "pending";
                break;
            default:
               
                responseMessage = "unknown";
                break;
        }

        String errorCode = "0"+randomValue;

        response.put("status", errorCode);
        response.put("message", responseMessage);

        // consume kafka message and return random response back to the kafka broker
        consumer.process(responseMessage);

        return Response.status(Response.Status.OK).entity(response.toString()).build();
    }
}
