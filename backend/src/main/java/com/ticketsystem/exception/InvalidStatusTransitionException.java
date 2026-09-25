package com.ticketsystem.exception;

import com.ticketsystem.domain.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    private final TicketStatus from;
    private final TicketStatus to;

    public InvalidStatusTransitionException(TicketStatus from, TicketStatus to, String message) {
        super(message);
        this.from = from;
        this.to = to;
    }

    public TicketStatus getFrom() {
        return from;
    }

    public TicketStatus getTo() {
        return to;
    }
}
