package com.zuhriddin.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Comment {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private UUID parentId;
    private String text;
    private LocalDateTime createdDateTime;

}
