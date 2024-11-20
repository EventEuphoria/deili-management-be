package com.deili.deilimanagement.card.dto;

import lombok.Data;

import java.util.List;

@Data
public class CardContentDto {
    private List<LabelDto> labels;
    private List<ChecklistDto> checklists;
    private List<CardAssigneeDto> cardAssignees;
}
