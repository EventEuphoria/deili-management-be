package com.deili.deilimanagement.lane.service;

import com.deili.deilimanagement.lane.dto.LaneDto;
import com.deili.deilimanagement.lane.entity.Lane;

import java.util.List;

public interface LaneService {
    LaneDto createLane(LaneDto laneDto);
    LaneDto updatelane(Long laneId, LaneDto laneDto);
    void deleteLane (Long laneId);
    List<LaneDto> getLanesByBoard(Long boardId);
    boolean reorderLanes(Long boardId, List<Long> laneIds);
    List<LaneDto> getAllLanes();
    LaneDto getLaneById(Long id);
}
