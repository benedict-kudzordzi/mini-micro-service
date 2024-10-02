package com.etz.kafka.serializer;

// import java.io.ByteArrayInputStream;
// import java.io.IOException;
// import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.etz.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDeserializer implements Deserializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    private final ObjectMapper objectMapper = new ObjectMapper();

    // @Override
    // public User deserialize(String topic, byte[] data) {
    //     try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
    //          ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
    //         return (User) objectInputStream.readObject();
    //     } catch (IOException | ClassNotFoundException e) {
    //         throw new RuntimeException("Error deserializing User", e);
    //     }
    // }

    @Override
    public User deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing User", e);
        }
    }

    
}
