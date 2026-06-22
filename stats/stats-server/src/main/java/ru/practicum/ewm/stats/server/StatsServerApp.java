package ru.practicum.ewm.stats.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.mappings.MappingDescriptionProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class StatsServerApp {
    public static void main(String[] args) {
        SpringApplication.run(StatsServerApp.class);
    }
}
