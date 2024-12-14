package com.deili.deilimanagement.card.dto;

import com.deili.deilimanagement.user.entity.JobRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardAssigneeDto {
    private Long id;
    private Long cardId;
    private Long userId;
    private String username;
    private String email;
    private JobRole jobRole;
}
