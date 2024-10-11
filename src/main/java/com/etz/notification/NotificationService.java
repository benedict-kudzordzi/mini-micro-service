package com.etz.notification;

import java.util.Random;

import org.json.JSONObject;

import com.etz.entity.User;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class NotificationService {
    private static final Random rand = new Random();

    
    public String getRandomResponse(User user) {
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

        response.put("error", errorCode);
        response.put("userId", user.getId());
        response.put("status", responseMessage);

        return response.toString();
    }
}
