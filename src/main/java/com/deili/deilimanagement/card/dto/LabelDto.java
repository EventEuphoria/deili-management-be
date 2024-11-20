package com.deili.deilimanagement.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDto {
    private Long id;
    private Long cardId;
    private List<LabelItemDto> labelItems;
}