package com.deili.deilimanagement.card.repository;

import com.deili.deilimanagement.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByLaneId(Long laneId);
}
