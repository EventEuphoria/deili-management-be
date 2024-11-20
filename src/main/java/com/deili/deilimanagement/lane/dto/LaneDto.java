// LaneDto.java
package com.deili.deilimanagement.lane.dto;

import lombok.Data;

@Data
public class LaneDto {
    private Long id;
    private String laneName;
    private Long boardId;
    private int position;
}
