package com.deili.deilimanagement.lane.service.Impl;

import com.deili.deilimanagement.board.entity.Board;
import com.deili.deilimanagement.board.repository.BoardRepository;
import com.deili.deilimanagement.exception.ResourceNotFoundException;
import com.deili.deilimanagement.lane.dto.LaneDto;
import com.deili.deilimanagement.lane.entity.Lane;
import com.deili.deilimanagement.lane.repository.LaneRepository;
import com.deili.deilimanagement.lane.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaneServiceImpl implements LaneService {

    private final LaneRepository laneRepository;
    private final BoardRepository boardRepository;

    @Override
    public LaneDto createLane(LaneDto laneDto) {
        Board board = boardRepository.findById(laneDto.getBoardId())
                .orElseThrow(() -> new ResourceNotFoundException("Board not found by Id "+laneDto.getBoardId()));
        Lane lane = new Lane();
        lane.setLaneName(laneDto.getLaneName());
        lane.setBoard(board);

        int maxPosition = board.getLane().stream()
                .mapToInt(Lane::getPosition)
                .max()
                .orElse(-1);
        lane.setPosition(maxPosition + 1);

        laneRepository.save(lane);
        laneDto.setId(lane.getId());
        return laneDto;
    }

    @Override
    public LaneDto updatelane(Long laneId, LaneDto laneDto) {
        Lane lane = laneRepository.findById(laneId)
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found with id "+laneId));
        lane.setLaneName(laneDto.getLaneName());
        laneRepository.save(lane);
        laneDto.setId(lane.getId());
        return laneDto;
    }

    @Override
    public void deleteLane(Long laneId) {
        Lane lane = laneRepository.findById(laneId)
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found with id "+laneId));
        laneRepository.delete(lane);
    }

    @Override
    public List<LaneDto> getLanesByBoard(Long boardId) {
        List<Lane> lanes = laneRepository.findByBoardId(boardId);
        return lanes.stream().map(lane -> {
            LaneDto laneDto = new LaneDto();
            laneDto.setId(lane.getId());
            laneDto.setLaneName(lane.getLaneName());
            laneDto.setBoardId(lane.getBoard().getId());
            laneDto.setPosition(lane.getPosition());
            return laneDto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean reorderLanes(Long boardId, List<Long> laneIds) {
        List<Lane> lanes = laneRepository.findByBoardId(boardId);

        if (lanes.isEmpty()) {
            throw new ResourceNotFoundException("No lanes found for board ID: " + boardId);
        }

        Map<Long, Lane> laneMap = lanes.stream()
                .collect(Collectors.toMap(Lane::getId, lane -> lane));

        lanes.forEach(lane -> lane.setPosition(lane.getPosition() + lanes.size()));
        laneRepository.saveAll(lanes);
        laneRepository.flush();

        for (int i = 0; i < laneIds.size(); i++) {
            Long laneId = laneIds.get(i);
            Lane lane = laneMap.get(laneId);

            if (lane == null) {
                throw new IllegalArgumentException("Invalid lane ID: " + laneId);
            }

            lane.setPosition(i);
        }

        laneRepository.saveAll(laneMap.values());
        laneRepository.flush();
        return true;
    }

    @Override
    public List<LaneDto> getAllLanes() {
        List<Lane> lanes = laneRepository.findAll();
        return lanes.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public LaneDto getLaneById(Long id) {
        Lane lane = laneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lane not found with id "+id));
        return mapToDto(lane);
    }

    private LaneDto mapToDto(Lane lane) {
        LaneDto laneDto = new LaneDto();
        laneDto.setId(lane.getId());
        laneDto.setLaneName(lane.getLaneName());
        laneDto.setBoardId(lane.getBoard().getId());
        laneDto.setPosition(lane.getPosition());
        return laneDto;
    }
}
