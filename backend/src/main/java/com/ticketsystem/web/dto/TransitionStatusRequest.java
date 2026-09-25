package com.ticketsystem.web.dto;

import com.ticketsystem.domain.TicketStatus;
import jakarta.validation.constraints.NotNull;

public class TransitionStatusRequest {

    @NotNull
    private TicketStatus status;

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
