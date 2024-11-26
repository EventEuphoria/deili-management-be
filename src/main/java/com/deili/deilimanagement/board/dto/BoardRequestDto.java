package com.deili.deilimanagement.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardRequestDto {
    private String boardName;
    private String boardDesc;
    private Boolean isComplete;
    private Long userId;
}
