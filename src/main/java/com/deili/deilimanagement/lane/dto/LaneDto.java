// LaneDto.java
package com.deili.deilimanagement.lane.dto;

import lombok.Data;

@Data
public class LaneDto {
    private Long id;
    private String laneName;
    private String laneDesc;
    private Long boardId;
    private int position;
}
