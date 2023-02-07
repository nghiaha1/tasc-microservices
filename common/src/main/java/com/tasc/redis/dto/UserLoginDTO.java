package com.tasc.redis.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash("login")
public class UserLoginDTO {
    @Id
    private String token;

    private long userId;

    private List<String> roles;

    private List<String> uris;

    private List<String> methods;

    @TimeToLive(unit = TimeUnit.DAYS)
    private long timeToLive;
}
