package ru.practicum.ewm.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsServerRepository extends JpaRepository<Hit, Long> {
}
