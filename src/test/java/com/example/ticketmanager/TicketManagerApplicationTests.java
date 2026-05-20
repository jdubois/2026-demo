package com.example.ticketmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
class TicketManagerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void listsSeededTickets() throws Exception {
		mockMvc
			.perform(get("/api/tickets"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(10)))
			.andExpect(jsonPath("$[0].assignee.username").value("julien"));
	}

	@Test
	void listsUsers() throws Exception {
		mockMvc
			.perform(get("/api/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[*].username", containsInAnyOrder("julien", "alice", "bob")));
	}

	@Test
	@DirtiesContext
	void createsTicket() throws Exception {
		String ticketJson = """
			{
			  "title": "Improve README onboarding",
			  "repository": "example/java-project",
			  "link": "https://github.com/example/java-project/issues/42",
			  "status": "IN_PROGRESS",
			  "assigneeId": 1
			}
			""";

		mockMvc
			.perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(ticketJson))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
			.andExpect(jsonPath("$.assignee.username").value("julien"));
	}

}
