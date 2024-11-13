package com.deili.deilimanagement.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private Long id;
    private String boardName;
    private String boardDesc;
    private boolean isComplete;
    private List<AssigneeDto> assignees;
}
