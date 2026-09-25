package com.ticketsystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketsystem.domain.Priority;
import com.ticketsystem.domain.Ticket;
import com.ticketsystem.domain.TicketStatus;
import com.ticketsystem.exception.InvalidPatchException;
import com.ticketsystem.exception.TicketNotFoundException;
import com.ticketsystem.repository.TicketRepository;
import com.ticketsystem.web.dto.CreateTicketRequest;
import com.ticketsystem.web.dto.TicketPageResponse;
import com.ticketsystem.web.dto.TicketResponse;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final TicketRepository ticketRepository;
    private final TicketStatusTransition statusTransition;

    public TicketService(TicketRepository ticketRepository, TicketStatusTransition statusTransition) {
        this.ticketRepository = ticketRepository;
        this.statusTransition = statusTransition;
    }

    @Transactional
    public Ticket create(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle().trim());
        ticket.setDescription(request.getDescription().trim());
        ticket.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        ticket.setStatus(TicketStatus.OPEN);
        if (request.getAssignee() != null && !request.getAssignee().isBlank()) {
            ticket.setAssignee(request.getAssignee().trim());
        }
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket getById(UUID id) {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public TicketPageResponse list(int page, int size) {
        int safeSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Ticket> result = ticketRepository.findAll(pageable);
        List<TicketResponse> content = result.getContent().stream().map(TicketResponse::from).toList();
        return new TicketPageResponse(content, result.getNumber(), result.getSize(), result.getTotalElements());
    }

    @Transactional
    public Ticket patch(UUID id, JsonNode body) {
        if (body == null || !body.isObject() || !body.fieldNames().hasNext()) {
            throw new InvalidPatchException("At least one field must be provided");
        }
        Ticket ticket = getById(id);
        Iterator<String> fields = body.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            switch (field) {
                case "title" -> applyTitle(ticket, body.get(field));
                case "description" -> applyDescription(ticket, body.get(field));
                case "priority" -> applyPriority(ticket, body.get(field));
                case "assignee" -> applyAssignee(ticket, body.get(field));
                case "status" -> throw new InvalidPatchException("Status cannot be updated via PATCH");
                default -> throw new InvalidPatchException("Unknown field: " + field);
            }
        }
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket transitionStatus(UUID id, TicketStatus targetStatus) {
        Ticket ticket = getById(id);
        TicketStatus current = ticket.getStatus();
        statusTransition.assertTransition(current, targetStatus);
        ticket.setStatus(targetStatus);
        return ticketRepository.save(ticket);
    }

    private void applyTitle(Ticket ticket, JsonNode node) {
        if (node.isNull()) {
            throw new InvalidPatchException("title must not be null");
        }
        String value = node.asText().trim();
        if (value.isEmpty()) {
            throw new InvalidPatchException("title must not be blank");
        }
        if (value.length() > 200) {
            throw new InvalidPatchException("title size must be between 0 and 200");
        }
        ticket.setTitle(value);
    }

    private void applyDescription(Ticket ticket, JsonNode node) {
        if (node.isNull()) {
            throw new InvalidPatchException("description must not be null");
        }
        String value = node.asText().trim();
        if (value.isEmpty()) {
            throw new InvalidPatchException("description must not be blank");
        }
        if (value.length() > 5000) {
            throw new InvalidPatchException("description size must be between 0 and 5000");
        }
        ticket.setDescription(value);
    }

    private void applyPriority(Ticket ticket, JsonNode node) {
        if (node.isNull()) {
            throw new InvalidPatchException("priority must not be null");
        }
        try {
            ticket.setPriority(Priority.valueOf(node.asText()));
        } catch (IllegalArgumentException ex) {
            throw new InvalidPatchException("Invalid priority value");
        }
    }

    private void applyAssignee(Ticket ticket, JsonNode node) {
        if (node.isNull()) {
            ticket.setAssignee(null);
            return;
        }
        String value = node.asText().trim();
        if (value.length() > 120) {
            throw new InvalidPatchException("assignee size must be between 0 and 120");
        }
        ticket.setAssignee(value.isEmpty() ? null : value);
    }
}
