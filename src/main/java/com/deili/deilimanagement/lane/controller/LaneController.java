package com.deili.deilimanagement.lane.controller;

import com.deili.deilimanagement.lane.dto.LaneDto;
import com.deili.deilimanagement.lane.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LaneController {
    private final LaneService laneService;

    @QueryMapping
    public List<LaneDto> getAllLanes(){
        return laneService.getAllLanes();
    }

    @QueryMapping
    public LaneDto getLaneById(@Argument Long id){
        return laneService.getLaneById(id);
    }

    @MutationMapping
    public LaneDto createLane(@Argument("laneDto") LaneDto laneDto){
        return laneService.createLane(laneDto);
    }

    @MutationMapping
    public LaneDto updateLane(@Argument Long laneId, @Argument("laneDto") LaneDto laneDto){
        return laneService.updatelane(laneId, laneDto);
    }

    @MutationMapping
    public boolean deleteLane(@Argument Long laneId){
        laneService.deleteLane(laneId);
        return true;
    }

    @QueryMapping
    public List<LaneDto> getLanesByBoard(@Argument Long boardId){
        return laneService.getLanesByBoard(boardId);
    }

    @MutationMapping
    public boolean reorderLanes(@Argument Long boardId, @Argument List<Long> laneIds) {
        try {
            laneService.reorderLanes(boardId, laneIds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
