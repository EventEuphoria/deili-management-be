package com.deili.deilimanagement.board.dto;

import com.deili.deilimanagement.board.entity.enums.BoardRole;
import lombok.Data;

@Data
public class AssigneeDto {
    private Long userId;
    private String userName;
    private BoardRole role;
}
