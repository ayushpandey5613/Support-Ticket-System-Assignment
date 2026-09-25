package com.ticketsystem.web.dto;

import com.ticketsystem.domain.Ticket;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.ticketsystem.domain.Priority;
import com.ticketsystem.domain.TicketStatus;

public record TicketDetailResponse(
        UUID id,
        String title,
        String description,
        Priority priority,
        TicketStatus status,
        String assignee,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> comments) {

    public static TicketDetailResponse from(Ticket ticket) {
        return new TicketDetailResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAssignee(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                List.of());
    }
}
