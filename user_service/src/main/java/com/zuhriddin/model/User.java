package com.zuhriddin.model;

import com.zuhriddin.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String imagePath;
    private String bio;
    private int age;
    private Gender gender;
}

