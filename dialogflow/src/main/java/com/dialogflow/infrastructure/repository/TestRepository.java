package com.dialogflow.infrastructure.repository;

import com.dialogflow.domain.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
