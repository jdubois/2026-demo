package com.example.ticketmanager.controller;

import com.example.ticketmanager.domain.Ticket;
import com.example.ticketmanager.repository.TicketRepository;
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

	TicketController(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
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
		return ticketRepository.save(request.toTicket());
	}

	@PutMapping("/{id}")
	Ticket update(@PathVariable Long id, @Valid @RequestBody TicketRequest request) {
		Ticket ticket = findTicket(id);
		ticket.setTitle(request.title());
		ticket.setRepository(request.repository());
		ticket.setLink(request.link());
		ticket.setStatus(request.status());
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

}
