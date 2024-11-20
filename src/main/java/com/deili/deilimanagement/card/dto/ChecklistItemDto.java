package com.deili.deilimanagement.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemDto {
    private Long id;
    private Long checklistId;
    private String content;
    private boolean status;
}