package com.example.ticketmanager.controller;

import com.example.ticketmanager.domain.AppUser;
import com.example.ticketmanager.domain.Ticket;
import com.example.ticketmanager.domain.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

record TicketRequest(
	@NotBlank @Size(max = 200) String title,
	@NotBlank @Size(max = 120) String repository,
	@NotBlank @Size(max = 300) @Pattern(regexp = "https://github\\.com/.+/.+/issues/\\d+") String link,
	@NotNull TicketStatus status,
	@NotNull Long assigneeId
) {

	Ticket toTicket(AppUser assignee) {
		return new Ticket(title, repository, link, status, assignee);
	}

}
