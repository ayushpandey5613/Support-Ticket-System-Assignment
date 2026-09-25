package com.ticketsystem.service;

import com.ticketsystem.domain.TicketStatus;
import com.ticketsystem.exception.InvalidStatusTransitionException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TicketStatusTransition {

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED = buildAllowed();

    private static Map<TicketStatus, Set<TicketStatus>> buildAllowed() {
        Map<TicketStatus, Set<TicketStatus>> map = new EnumMap<>(TicketStatus.class);
        map.put(TicketStatus.OPEN, EnumSet.of(TicketStatus.IN_PROGRESS, TicketStatus.CANCELLED));
        map.put(TicketStatus.IN_PROGRESS, EnumSet.of(TicketStatus.RESOLVED, TicketStatus.CANCELLED));
        map.put(TicketStatus.RESOLVED, EnumSet.of(TicketStatus.CLOSED));
        map.put(TicketStatus.CLOSED, EnumSet.noneOf(TicketStatus.class));
        map.put(TicketStatus.CANCELLED, EnumSet.noneOf(TicketStatus.class));
        return map;
    }

    public void assertTransition(TicketStatus from, TicketStatus to) {
        if (from == to) {
            throw new InvalidStatusTransitionException(from, to, "Cannot transition to the same status");
        }
        Set<TicketStatus> targets = ALLOWED.getOrDefault(from, EnumSet.noneOf(TicketStatus.class));
        if (!targets.contains(to)) {
            throw new InvalidStatusTransitionException(
                    from, to, "Cannot transition from " + from + " to " + to);
        }
    }

    public boolean isAllowed(TicketStatus from, TicketStatus to) {
        if (from == to) {
            return false;
        }
        return ALLOWED.getOrDefault(from, EnumSet.noneOf(TicketStatus.class)).contains(to);
    }
}
