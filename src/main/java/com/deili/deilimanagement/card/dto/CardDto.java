package com.deili.deilimanagement.card.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardDto {
    private Long id;
    private String cardName;
    private String cardDesc;
    private LocalDateTime dueDate;
    private Long laneId;
    private Long position;
}
