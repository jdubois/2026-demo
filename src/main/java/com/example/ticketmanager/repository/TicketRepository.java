package com.example.ticketmanager.repository;

import com.example.ticketmanager.domain.Ticket;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByAssigneeIsNull(Sort sort);

}
