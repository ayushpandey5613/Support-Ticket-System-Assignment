package com.ticketsystem.persistence;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.TicketSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * IT-08: ticket data survives a full application restart against PostgreSQL (postgres profile).
 */
class TicketRestartPersistenceIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void ticketSurvivesApplicationRestart_onPostgres() throws Exception {
        assumeTrue(
                DockerClientFactory.instance().isDockerAvailable(),
                "Docker not available — skipping IT-08");

        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("tickets")
                .withUsername("ticket")
                .withPassword("ticket")) {
            postgres.start();

            String ticketId;
            ConfigurableApplicationContext first = startPostgresApp(postgres);
            try {
                MockMvc mockMvc = mockMvcFor(first);
                String createJson = """
                        {
                          "title": "Persist after restart",
                          "description": "IT-08 postgres",
                          "priority": "MEDIUM"
                        }
                        """;
                MvcResult created = mockMvc.perform(post("/api/v1/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.status", is("OPEN")))
                        .andReturn();
                JsonNode body = OBJECT_MAPPER.readTree(created.getResponse().getContentAsString());
                ticketId = body.get("id").asText();
            } finally {
                first.close();
            }

            ConfigurableApplicationContext second = startPostgresApp(postgres);
            try {
                MockMvc mockMvc = mockMvcFor(second);
                mockMvc.perform(get("/api/v1/tickets/{id}", ticketId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(ticketId)))
                        .andExpect(jsonPath("$.title", is("Persist after restart")))
                        .andExpect(jsonPath("$.description", is("IT-08 postgres")));
            } finally {
                second.close();
            }
        }
    }

    private static ConfigurableApplicationContext startPostgresApp(PostgreSQLContainer<?> postgres) {
        return SpringApplication.run(
                TicketSystemApplication.class,
                "--spring.profiles.active=postgres",
                "--server.port=0",
                "--spring.datasource.url=" + postgres.getJdbcUrl(),
                "--spring.datasource.username=" + postgres.getUsername(),
                "--spring.datasource.password=" + postgres.getPassword());
    }

    private static MockMvc mockMvcFor(ConfigurableApplicationContext ctx) {
        WebApplicationContext webCtx = (WebApplicationContext) ctx;
        return MockMvcBuilders.webAppContextSetup(webCtx).build();
    }
}
