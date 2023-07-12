package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Test;
import com.dialogflow.infrastructure.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public List<Test> findTests() {
        return testRepository.findAll();
    }
}
