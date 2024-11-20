package com.deili.deilimanagement.card.repository;

import com.deili.deilimanagement.card.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByCardId(Long cardId);
}
