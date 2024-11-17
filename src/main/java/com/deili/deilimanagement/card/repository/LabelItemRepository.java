package com.deili.deilimanagement.card.repository;

import com.deili.deilimanagement.card.entity.LabelItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelItemRepository extends JpaRepository<LabelItem, Long> {
}
