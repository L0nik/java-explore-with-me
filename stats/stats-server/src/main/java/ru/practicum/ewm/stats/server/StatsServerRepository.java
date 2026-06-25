package ru.practicum.ewm.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsServerRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h.app as app, h.uri as uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT h.ip) ELSE COUNT(h.ip) END as hits " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri " +
            "SORT BT hits DESC"
    )
    public Collection<StatsView> getStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") Collection<String> uris,
            @Param("unique") Boolean unique
    );

}
