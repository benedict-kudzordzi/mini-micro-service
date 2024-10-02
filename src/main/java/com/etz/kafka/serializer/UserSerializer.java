package com.etz.kafka.serializer;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.etz.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserSerializer implements Serializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    private final ObjectMapper objectMapper = new ObjectMapper();

    // @Override
    // public byte[] serialize(String topic, User data) {
    //     try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    //          ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
    //         objectOutputStream.writeObject(data);
    //         return byteArrayOutputStream.toByteArray();
    //     } catch (IOException e) {
    //         throw new RuntimeException("Error serializing User", e);
    //     }
    // }

    @Override
    public byte[] serialize(String topic, User user) {
        try {
            return objectMapper.writeValueAsBytes(user);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing User", e);
        }
    }

  
    
}
