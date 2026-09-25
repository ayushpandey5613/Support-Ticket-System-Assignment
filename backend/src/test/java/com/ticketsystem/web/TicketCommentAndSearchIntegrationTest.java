package com.ticketsystem.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
class TicketCommentAndSearchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postComment_appearsOnGetDetail() throws Exception {
        String ticketId = createTicket("Billing issue", "Invoice wrong amount");

        mockMvc.perform(post("/api/v1/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"author":"Agent","body":"We are checking"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId", is(ticketId)))
                .andExpect(jsonPath("$.author", is("Agent")));

        mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].body", is("We are checking")))
                .andExpect(jsonPath("$.comments[0].ticketId").doesNotExist());
    }

    @Test
    void list_filterByStatus() throws Exception {
        createTicket("StatusFilterOpenMarker", "desc");
        String inProgressId = createTicket("StatusFilterProgMarker", "desc");
        mockMvc.perform(post("/api/v1/tickets/{id}/status", inProgressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tickets")
                        .param("status", "IN_PROGRESS")
                        .param("q", "StatusFilterProgMarker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(inProgressId)))
                .andExpect(jsonPath("$.content[0].status", is("IN_PROGRESS")));
    }

    @Test
    void list_searchByKeyword_matchesTitleOrDescription() throws Exception {
        createTicket("UniqueAlphaTitle", "generic desc");
        createTicket("other", "contains UniqueBetaWord here");

        mockMvc.perform(get("/api/v1/tickets").param("q", "uniquealpha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("UniqueAlphaTitle")));

        mockMvc.perform(get("/api/v1/tickets").param("q", "UniqueBetaWord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("other")));
    }

    @Test
    void list_searchAndStatus_combinedWithAnd() throws Exception {
        String matchId = createTicket("SearchAndStatus", "help");
        mockMvc.perform(post("/api/v1/tickets/{id}/status", matchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk());
        createTicket("SearchAndStatus", "still open");

        mockMvc.perform(get("/api/v1/tickets").param("q", "SearchAndStatus").param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(matchId)));
    }

    @Test
    void addComment_blankBody_returns400() throws Exception {
        String ticketId = createTicket("T", "D");
        mockMvc.perform(post("/api/v1/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"A\",\"body\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    @Test
    void list_invalidStatusParam_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/tickets").param("status", "NOT_A_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    @Test
    void addComment_unknownTicket_returns404() throws Exception {
        mockMvc.perform(post("/api/v1/tickets/{id}/comments", "00000000-0000-0000-0000-000000000099")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"A\",\"body\":\"B\"}"))
                .andExpect(status().isNotFound());
    }

    private String createTicket(String title, String description) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                java.util.Map.of("title", title, "description", description))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }
}
