package com.example.ticketmanager.config;

import com.example.ticketmanager.domain.AppUser;
import com.example.ticketmanager.domain.Ticket;
import com.example.ticketmanager.domain.TicketStatus;
import com.example.ticketmanager.repository.TicketRepository;
import com.example.ticketmanager.repository.UserRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

@Configuration(proxyBeanMethods = false)
class TicketSeedConfiguration {

	private static final List<String> USERNAMES = List.of("julien", "alice", "bob");

	@Bean
	ApplicationRunner seedTickets(TicketRepository ticketRepository, UserRepository userRepository) {
		return args -> {
			List<AppUser> users = seedUsers(userRepository);

			if (ticketRepository.count() > 0) {
				assignUnassignedTickets(ticketRepository, users);
				return;
			}

			AtomicInteger assigneeIndex = new AtomicInteger();
			ticketRepository.saveAll(
				List.of(
					ticket(
						"Add xml validation for the stored characters",
						"Antafes/Vampire-Editor",
						"https://github.com/Antafes/Vampire-Editor/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"[Test] Add tests for DependencyNode",
						"yasmramos/Veld",
						"https://github.com/yasmramos/Veld/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Add DSA Question For Rust",
						"xthxr/OpenSauce",
						"https://github.com/xthxr/OpenSauce/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Update Joystick Scaling/Deadband",
						"RoboLoCo-5338/Competition_Bot_2025",
						"https://github.com/RoboLoCo-5338/Competition_Bot_2025/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Mod \"X\" doesn't work",
						"PojavLauncherTeam/PojavLauncher",
						"https://github.com/PojavLauncherTeam/PojavLauncher/issues/1948",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Create DappNode package for hildr",
						"optimism-java/hildr",
						"https://github.com/optimism-java/hildr/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Move log (PhasedTestListener.java:317) - [Phased Testing] Reducing Report for to debug",
						"adobe/phased-testing",
						"https://github.com/adobe/phased-testing/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Refactor/optimize code related to calculating the DefaultHashAlgorithms",
						"DataONEorg/hashstore-java",
						"https://github.com/DataONEorg/hashstore-java/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"Split calls should be replaced with Regular Expressions",
						"symt/BazaarNotifier",
						"https://github.com/symt/BazaarNotifier/issues/100",
						nextAssignee(users, assigneeIndex)
					),
					ticket(
						"요청에 대한 응답을 세분화 한다.",
						"mymateM/AcountApp",
						"https://github.com/mymateM/AcountApp/issues/100",
						nextAssignee(users, assigneeIndex)
					)
				)
			);
		};
	}

	private static List<AppUser> seedUsers(UserRepository userRepository) {
		return USERNAMES
			.stream()
			.map(username ->
				userRepository.findByUsername(username).orElseGet(() -> userRepository.save(new AppUser(username)))
			)
			.toList();
	}

	private static void assignUnassignedTickets(TicketRepository ticketRepository, List<AppUser> users) {
		AtomicInteger assigneeIndex = new AtomicInteger();
		List<Ticket> unassignedTickets = ticketRepository.findByAssigneeIsNull(Sort.by(Sort.Direction.ASC, "id"));
		unassignedTickets.forEach(ticket -> ticket.setAssignee(nextAssignee(users, assigneeIndex)));
		ticketRepository.saveAll(unassignedTickets);
	}

	private static AppUser nextAssignee(List<AppUser> users, AtomicInteger assigneeIndex) {
		return users.get(assigneeIndex.getAndIncrement() % users.size());
	}

	private static Ticket ticket(String title, String repository, String link, AppUser assignee) {
		return new Ticket(title, repository, link, TicketStatus.OPEN, assignee);
	}

}
