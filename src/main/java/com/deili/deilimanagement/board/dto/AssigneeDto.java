package com.deili.deilimanagement.board.dto;

import com.deili.deilimanagement.board.entity.enums.BoardRole;
import com.deili.deilimanagement.board.entity.enums.InvitationStatus;
import lombok.Data;

@Data
public class AssigneeDto {
    private Long userId;
    private String userName;
    private BoardRole role;
    private String email;
    private InvitationStatus status;
}
