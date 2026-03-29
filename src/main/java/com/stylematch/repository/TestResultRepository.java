package com.stylematch.repository;

import com.stylematch.domain.TestResult;
import com.stylematch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserOrderByCreatedAtDesc(User user);
    List<TestResult> findByUserAndTestTypeOrderByCreatedAtDesc(User user, String testType);
    java.util.Optional<TestResult> findByIdAndUser(Long id, User user);
    void deleteByUser(User user);
    void deleteByUserAndTestType(User user, String testType);
}
