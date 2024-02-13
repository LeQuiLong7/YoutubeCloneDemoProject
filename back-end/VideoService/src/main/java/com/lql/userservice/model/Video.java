package com.lql.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Video {

    @Id
    private String id;
    private String s3Id;
    private String videoURL;

    private String s3ThumbnailId;
    private String thumbnailURL;

    private String title;
    private String description;
    private int totalViews;
    private List<Comment> comments;

}
