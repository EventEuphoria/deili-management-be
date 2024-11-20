package com.deili.deilimanagement.board.repository;

import com.deili.deilimanagement.board.entity.BoardAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardAssigneeRepository extends JpaRepository<BoardAssignee, Long> {
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
    Optional<BoardAssignee> findByBoardIdAndUserId(Long boardId, Long userId);
}
