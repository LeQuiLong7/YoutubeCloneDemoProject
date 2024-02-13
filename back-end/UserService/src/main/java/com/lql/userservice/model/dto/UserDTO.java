package com.lql.userservice.model.dto;

import com.lql.userservice.model.User;

public record UserDTO (String userId, String chanelName) {

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getChanelName());
    }
}
