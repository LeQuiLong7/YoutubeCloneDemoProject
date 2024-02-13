package com.lql.userservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public final class SubscriptionRequest {

    @NotNull
    @NotBlank
    private  String subscriberId;
    @NotNull
    @NotBlank
    private  String subscribeToId;
}
