package com.ticketsystem.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketStatusTransitionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validChain_openToClosed_succeeds() throws Exception {
        String id = createTicket();
        transition(id, "IN_PROGRESS").andExpect(status().isOk());
        transition(id, "RESOLVED").andExpect(status().isOk());
        transition(id, "CLOSED").andExpect(status().isOk()).andExpect(jsonPath("$.status", is("CLOSED")));
    }

    @Test
    void openToCancelled_succeeds() throws Exception {
        String id = createTicket();
        transition(id, "CANCELLED").andExpect(status().isOk()).andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    @Test
    void closedToOpen_returns409() throws Exception {
        String id = createTicket();
        transition(id, "IN_PROGRESS");
        transition(id, "RESOLVED");
        transition(id, "CLOSED");

        transition(id, "OPEN")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("INVALID_STATUS_TRANSITION")))
                .andExpect(jsonPath("$.from", is("CLOSED")))
                .andExpect(jsonPath("$.to", is("OPEN")));
    }

    @ParameterizedTest
    @CsvSource({
        "OPEN, RESOLVED",
        "OPEN, CLOSED",
        "RESOLVED, CANCELLED",
        "CANCELLED, OPEN"
    })
    void invalidTransitions_return409(String fromStatus, String toStatus) throws Exception {
        String id = createTicketInStatus(fromStatus);
        transition(id, toStatus)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("INVALID_STATUS_TRANSITION")))
                .andExpect(jsonPath("$.from", is(fromStatus)))
                .andExpect(jsonPath("$.to", is(toStatus)));
    }

    @Test
    void missingStatus_returns400() throws Exception {
        String id = createTicket();
        mockMvc.perform(post("/api/v1/tickets/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    private String createTicket() throws Exception {
        return createTicketInStatus("OPEN");
    }

    private String createTicketInStatus(String targetStatus) throws Exception {
        MvcResult create = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"T","description":"D"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String id = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asText();
        if ("OPEN".equals(targetStatus)) {
            return id;
        }
        if ("IN_PROGRESS".equals(targetStatus)) {
            transition(id, "IN_PROGRESS");
            return id;
        }
        if ("RESOLVED".equals(targetStatus)) {
            transition(id, "IN_PROGRESS");
            transition(id, "RESOLVED");
            return id;
        }
        if ("CLOSED".equals(targetStatus)) {
            transition(id, "IN_PROGRESS");
            transition(id, "RESOLVED");
            transition(id, "CLOSED");
            return id;
        }
        if ("CANCELLED".equals(targetStatus)) {
            transition(id, "CANCELLED");
            return id;
        }
        throw new IllegalArgumentException("Unsupported status: " + targetStatus);
    }

    private org.springframework.test.web.servlet.ResultActions transition(String id, String status) throws Exception {
        return mockMvc.perform(post("/api/v1/tickets/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"" + status + "\"}"));
    }
}
