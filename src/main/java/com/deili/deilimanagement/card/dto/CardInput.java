package com.deili.deilimanagement.card.dto;

import lombok.Data;

@Data
public class CardInput {
    private String cardName;
    private String cardDesc;
    private Long laneId;
}
