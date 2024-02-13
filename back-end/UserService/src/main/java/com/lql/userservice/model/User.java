package com.lql.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

    @Id
    private String id;
    private String oauthId;

    private String chanelName;

    private List<String> videoIds;

    private Set<String> subscribersIds;
    private Set<String> subscribedToIds;

    private List<String> watchHistory;
    private List<String> likedHistory;
}
