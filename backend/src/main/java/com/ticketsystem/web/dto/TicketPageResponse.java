package com.ticketsystem.web.dto;

import java.util.List;

public record TicketPageResponse(List<TicketResponse> content, int page, int size, long totalElements) {
}
