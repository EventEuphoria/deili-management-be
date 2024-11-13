package com.deili.deilimanagement.board.dto;

import com.deili.deilimanagement.board.entity.enums.BoardRole;
import lombok.Data;

@Data
public class AssigneeRequest {
    private Long userId;
    private BoardRole boardRole;
}
