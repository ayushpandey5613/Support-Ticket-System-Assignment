package com.ticketsystem.web.dto;

import com.ticketsystem.domain.Priority;
import com.ticketsystem.domain.Ticket;
import com.ticketsystem.domain.TicketStatus;
import java.time.Instant;
import java.util.UUID;

public class TicketResponse {

    private UUID id;
    private String title;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private String assignee;
    private Instant createdAt;
    private Instant updatedAt;

    public static TicketResponse from(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.id = ticket.getId();
        response.title = ticket.getTitle();
        response.description = ticket.getDescription();
        response.priority = ticket.getPriority();
        response.status = ticket.getStatus();
        response.assignee = ticket.getAssignee();
        response.createdAt = ticket.getCreatedAt();
        response.updatedAt = ticket.getUpdatedAt();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getAssignee() {
        return assignee;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
