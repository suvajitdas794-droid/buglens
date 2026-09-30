package com.buglens.repository;
import com.buglens.model.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AnalysisRepository extends JpaRepository<Analysis,Long> {
    List<Analysis> findTop20ByOrderByCreatedAtDesc();
    long countBySeverity(String severity);
    long countByCategory(String category);
}