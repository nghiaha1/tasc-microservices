package com.tasc.user.service;

import com.tasc.user.repository.UriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    @Autowired
    private UriRepository uriRepository;
}
