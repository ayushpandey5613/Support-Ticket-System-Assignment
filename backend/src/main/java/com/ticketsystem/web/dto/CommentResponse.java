package com.ticketsystem.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        @JsonInclude(JsonInclude.Include.NON_NULL) UUID ticketId,
        String author,
        String body,
        Instant createdAt) {

    public static CommentResponse forDetail(UUID id, String author, String body, Instant createdAt) {
        return new CommentResponse(id, null, author, body, createdAt);
    }
}
