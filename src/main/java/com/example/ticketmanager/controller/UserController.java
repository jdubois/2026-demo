package com.example.ticketmanager.controller;

import com.example.ticketmanager.domain.AppUser;
import com.example.ticketmanager.repository.UserRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
class UserController {

	private final UserRepository userRepository;

	UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	List<AppUser> list() {
		return userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
	}

}
