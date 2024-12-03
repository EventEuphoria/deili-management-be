package com.deili.deilimanagement.board.repository;

import com.deili.deilimanagement.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b JOIN b.boardAssignees ba WHERE ba.user.id = :userId")
    List<Board> findByUserId(@Param("userId") Long userId);
}
