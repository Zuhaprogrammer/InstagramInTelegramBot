package com.zuhriddin.model;

import com.zuhriddin.enumeration.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {
    private int step;
    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private String location;
    private String path;
    private PostType postType;
    private Date createDate;
}