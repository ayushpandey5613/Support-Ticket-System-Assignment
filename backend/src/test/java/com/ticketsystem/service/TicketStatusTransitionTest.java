package com.ticketsystem.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ticketsystem.domain.TicketStatus;
import com.ticketsystem.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TicketStatusTransitionTest {

    private TicketStatusTransition transition;

    @BeforeEach
    void setUp() {
        transition = new TicketStatusTransition();
    }

    @ParameterizedTest
    @CsvSource({
        "OPEN, IN_PROGRESS",
        "OPEN, CANCELLED",
        "IN_PROGRESS, RESOLVED",
        "IN_PROGRESS, CANCELLED",
        "RESOLVED, CLOSED"
    })
    void assertTransition_allowsValidEdges(TicketStatus from, TicketStatus to) {
        assertDoesNotThrow(() -> transition.assertTransition(from, to));
        assertTrue(transition.isAllowed(from, to));
    }

    @ParameterizedTest
    @CsvSource({
        "CLOSED, OPEN",
        "RESOLVED, OPEN",
        "CANCELLED, OPEN",
        "CLOSED, IN_PROGRESS",
        "RESOLVED, IN_PROGRESS",
        "OPEN, CLOSED",
        "OPEN, RESOLVED",
        "RESOLVED, CANCELLED",
        "CANCELLED, CLOSED",
        "CLOSED, CANCELLED"
    })
    void assertTransition_rejectsForbiddenEdges(TicketStatus from, TicketStatus to) {
        InvalidStatusTransitionException ex =
                assertThrows(InvalidStatusTransitionException.class, () -> transition.assertTransition(from, to));
        assertFalse(transition.isAllowed(from, to));
        org.junit.jupiter.api.Assertions.assertEquals(from, ex.getFrom());
        org.junit.jupiter.api.Assertions.assertEquals(to, ex.getTo());
    }

    @Test
    void assertTransition_rejectsSameState() {
        assertThrows(InvalidStatusTransitionException.class, () -> transition.assertTransition(TicketStatus.OPEN, TicketStatus.OPEN));
    }
}
