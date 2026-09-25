package com.ticketsystem.exception;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {

    private final UUID ticketId;

    public TicketNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
        this.ticketId = ticketId;
    }

    public UUID getTicketId() {
        return ticketId;
    }
}
