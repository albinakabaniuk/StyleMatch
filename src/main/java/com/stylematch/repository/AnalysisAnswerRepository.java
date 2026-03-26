package com.stylematch.repository;

import com.stylematch.domain.AnalysisAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnalysisAnswerRepository extends JpaRepository<AnalysisAnswer, UUID> {
}
