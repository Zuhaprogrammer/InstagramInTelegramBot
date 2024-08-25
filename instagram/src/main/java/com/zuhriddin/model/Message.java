package com.zuhriddin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private UUID id;
    private String text;
    private UUID fromId;
    private UUID toId;
    private LocalDateTime createDate; //yozilgan messagening vaqtini korsataydigan method;

}
