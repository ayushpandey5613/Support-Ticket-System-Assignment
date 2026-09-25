package com.ticketsystem.service;

import com.ticketsystem.domain.Comment;
import com.ticketsystem.domain.Ticket;
import com.ticketsystem.exception.TicketNotFoundException;
import com.ticketsystem.repository.CommentRepository;
import com.ticketsystem.repository.TicketRepository;
import com.ticketsystem.web.dto.CommentResponse;
import com.ticketsystem.web.dto.CreateCommentRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public CommentResponse addComment(UUID ticketId, CreateCommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setAuthor(request.getAuthor().trim());
        comment.setBody(request.getBody().trim());
        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> listForTicket(UUID ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException(ticketId);
        }
        return commentRepository.findByTicket_IdOrderByCreatedAtAsc(ticketId).stream()
                .map(this::toDetailResponse)
                .toList();
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTicketId(),
                comment.getAuthor(),
                comment.getBody(),
                comment.getCreatedAt());
    }

    private CommentResponse toDetailResponse(Comment comment) {
        return CommentResponse.forDetail(
                comment.getId(), comment.getAuthor(), comment.getBody(), comment.getCreatedAt());
    }
}
