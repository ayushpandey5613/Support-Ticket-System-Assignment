package com.ticketsystem.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketsystem.domain.Ticket;
import com.ticketsystem.domain.TicketStatus;
import com.ticketsystem.service.CommentService;
import com.ticketsystem.service.TicketService;
import com.ticketsystem.web.dto.CreateCommentRequest;
import com.ticketsystem.web.dto.CommentResponse;
import com.ticketsystem.web.dto.CreateTicketRequest;
import com.ticketsystem.web.dto.TicketDetailResponse;
import com.ticketsystem.web.dto.TicketPageResponse;
import com.ticketsystem.web.dto.TicketResponse;
import com.ticketsystem.web.dto.TransitionStatusRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;

    public TicketController(TicketService ticketService, CommentService commentService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.from(ticket));
    }

    @GetMapping
    public TicketPageResponse list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String q) {
        return ticketService.list(page, size, status, q);
    }

    @GetMapping("/{id}")
    public TicketDetailResponse getById(@PathVariable UUID id) {
        Ticket ticket = ticketService.getById(id);
        return TicketDetailResponse.from(ticket, commentService.listForTicket(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID id, @Valid @RequestBody CreateCommentRequest request) {
        CommentResponse comment = commentService.addComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{id}")
    public TicketResponse patch(@PathVariable UUID id, @RequestBody JsonNode body) {
        Ticket ticket = ticketService.patch(id, body);
        return TicketResponse.from(ticket);
    }

    @PostMapping("/{id}/status")
    public TicketResponse transitionStatus(
            @PathVariable UUID id, @Valid @RequestBody TransitionStatusRequest request) {
        Ticket ticket = ticketService.transitionStatus(id, request.getStatus());
        return TicketResponse.from(ticket);
    }
}
