package com.zuhriddin.model;

import com.zuhriddin.model.enumeration.PostState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {
    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private String location;
    private String photoPath;
    private PostState postState;
    private LocalDateTime createDate;
}