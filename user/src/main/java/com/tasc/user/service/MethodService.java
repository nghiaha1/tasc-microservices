package com.tasc.user.service;

import com.tasc.user.repository.MethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MethodService {

    @Autowired
    private MethodRepository methodRepository;
}
