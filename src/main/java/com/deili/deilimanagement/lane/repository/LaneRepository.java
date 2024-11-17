package com.deili.deilimanagement.lane.repository;

import com.deili.deilimanagement.lane.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaneRepository extends JpaRepository<Lane, Long> {
    List<Lane> findByBoardId(Long boardId);
}
