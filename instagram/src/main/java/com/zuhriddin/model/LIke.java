package com.zuhriddin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LIke {
    private UUID ID;
    private UUID userID;
    private UUID postID;
    private LocalDate creatDate;//qachon like bosilgan sana;

}
