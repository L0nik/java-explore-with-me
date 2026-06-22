package ru.practicum.ewm.stats.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsServerController {

    @PostMapping
    public void hit() {
        //
    }

    @GetMapping
    public void getStats() {
        //
    }
}
