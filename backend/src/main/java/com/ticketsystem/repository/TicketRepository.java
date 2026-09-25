package com.ticketsystem.repository;

import com.ticketsystem.domain.Ticket;
import com.ticketsystem.domain.TicketStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(
            """
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (
                :q IS NULL
                OR LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<Ticket> findFiltered(
            @Param("status") TicketStatus status,
            @Param("q") String q,
            Pageable pageable);
}
