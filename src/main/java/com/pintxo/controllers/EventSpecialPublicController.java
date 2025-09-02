package com.pintxo.controllers;

import com.pintxo.dtos.EventPublicDTO;
import com.pintxo.services.EventSpecialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events-specials")
@RequiredArgsConstructor
public class EventSpecialPublicController {

    private final EventSpecialService service;

    @GetMapping
    public ResponseEntity<List<EventPublicDTO>> list(@RequestParam(required=false) Integer limit){
        return ResponseEntity.ok(service.listPublic(limit));
    }
}