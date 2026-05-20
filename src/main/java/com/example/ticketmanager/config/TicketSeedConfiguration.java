package com.example.ticketmanager.config;

import com.example.ticketmanager.domain.Ticket;
import com.example.ticketmanager.domain.TicketStatus;
import com.example.ticketmanager.repository.TicketRepository;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class TicketSeedConfiguration {

	@Bean
	ApplicationRunner seedTickets(TicketRepository ticketRepository) {
		return args -> {
			if (ticketRepository.count() > 0) {
				return;
			}

			ticketRepository.saveAll(
				List.of(
					ticket(
						"Add xml validation for the stored characters",
						"Antafes/Vampire-Editor",
						"https://github.com/Antafes/Vampire-Editor/issues/100"
					),
					ticket(
						"[Test] Add tests for DependencyNode",
						"yasmramos/Veld",
						"https://github.com/yasmramos/Veld/issues/100"
					),
					ticket(
						"Add DSA Question For Rust",
						"xthxr/OpenSauce",
						"https://github.com/xthxr/OpenSauce/issues/100"
					),
					ticket(
						"Update Joystick Scaling/Deadband",
						"RoboLoCo-5338/Competition_Bot_2025",
						"https://github.com/RoboLoCo-5338/Competition_Bot_2025/issues/100"
					),
					ticket(
						"Mod \"X\" doesn't work",
						"PojavLauncherTeam/PojavLauncher",
						"https://github.com/PojavLauncherTeam/PojavLauncher/issues/1948"
					),
					ticket(
						"Create DappNode package for hildr",
						"optimism-java/hildr",
						"https://github.com/optimism-java/hildr/issues/100"
					),
					ticket(
						"Move log (PhasedTestListener.java:317) - [Phased Testing] Reducing Report for to debug",
						"adobe/phased-testing",
						"https://github.com/adobe/phased-testing/issues/100"
					),
					ticket(
						"Refactor/optimize code related to calculating the DefaultHashAlgorithms",
						"DataONEorg/hashstore-java",
						"https://github.com/DataONEorg/hashstore-java/issues/100"
					),
					ticket(
						"Split calls should be replaced with Regular Expressions",
						"symt/BazaarNotifier",
						"https://github.com/symt/BazaarNotifier/issues/100"
					),
					ticket(
						"요청에 대한 응답을 세분화 한다.",
						"mymateM/AcountApp",
						"https://github.com/mymateM/AcountApp/issues/100"
					)
				)
			);
		};
	}

	private static Ticket ticket(String title, String repository, String link) {
		return new Ticket(title, repository, link, TicketStatus.OPEN);
	}

}
