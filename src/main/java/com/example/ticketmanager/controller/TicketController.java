package com.example.ticketmanager.controller;

import com.example.ticketmanager.domain.AppUser;
import com.example.ticketmanager.domain.Ticket;
import com.example.ticketmanager.repository.TicketRepository;
import com.example.ticketmanager.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tickets")
class TicketController {

	private final TicketRepository ticketRepository;

	private final UserRepository userRepository;

	TicketController(TicketRepository ticketRepository, UserRepository userRepository) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
	}

	@GetMapping
	List<Ticket> list() {
		return ticketRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@GetMapping("/{id}")
	Ticket get(@PathVariable Long id) {
		return findTicket(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Ticket create(@Valid @RequestBody TicketRequest request) {
		return ticketRepository.save(request.toTicket(findAssignee(request.assigneeId())));
	}

	@PutMapping("/{id}")
	Ticket update(@PathVariable Long id, @Valid @RequestBody TicketRequest request) {
		Ticket ticket = findTicket(id);
		ticket.setTitle(request.title());
		ticket.setRepository(request.repository());
		ticket.setLink(request.link());
		ticket.setStatus(request.status());
		ticket.setAssignee(findAssignee(request.assigneeId()));
		return ticketRepository.save(ticket);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable Long id) {
		if (!ticketRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
		}
		ticketRepository.deleteById(id);
	}

	private Ticket findTicket(Long id) {
		return ticketRepository
			.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
	}

	private AppUser findAssignee(Long assigneeId) {
		return userRepository
			.findById(assigneeId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown assigneeId"));
	}

}
