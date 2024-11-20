package com.deili.deilimanagement.card.repository;

import com.deili.deilimanagement.card.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByCardId(Long cardid);
}
