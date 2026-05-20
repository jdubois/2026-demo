package com.example.ticketmanager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 200)
	@Column(nullable = false, length = 200)
	private String title;

	@NotBlank
	@Size(max = 120)
	@Column(nullable = false, length = 120)
	private String repository;

	@NotBlank
	@Size(max = 300)
	@Pattern(regexp = "https://github\\.com/.+/.+/issues/\\d+")
	@Column(nullable = false, unique = true, length = 300)
	private String link;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private TicketStatus status = TicketStatus.OPEN;

	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private AppUser assignee;

	protected Ticket() {
	}

	public Ticket(String title, String repository, String link, TicketStatus status, AppUser assignee) {
		this.title = title;
		this.repository = repository;
		this.link = link;
		this.status = status;
		this.assignee = assignee;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public AppUser getAssignee() {
		return assignee;
	}

	public void setAssignee(AppUser assignee) {
		this.assignee = assignee;
	}

}
