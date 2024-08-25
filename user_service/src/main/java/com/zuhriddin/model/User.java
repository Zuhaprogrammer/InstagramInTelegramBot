package com.zuhriddin.model;

import com.zuhriddin.enumeration.Gender;
import com.zuhriddin.enumeration.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private UUID id;
    private Long chatId;
    private String name;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String imagePath;
    private String bio;
    private LocalDate birthDate;
    private String link;
    private LocalDateTime createdDate;
    private LocalDateTime lastActiveTime;
    private Gender gender;
    private UserState userState;
}

