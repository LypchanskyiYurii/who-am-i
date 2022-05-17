package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.configuration.GameControllerAdvice;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

	private final GameServiceImpl gameService = mock(GameServiceImpl.class);
	private final GameController gameController = new GameController(gameService);
	private final NewGameRequest gameRequest = new NewGameRequest();
	private MockMvc mockMvc;
	private final String id = "33333";

	@BeforeEach
	public void setMockMvc() {
		mockMvc = MockMvcBuilders.standaloneSetup(gameController)
				.setControllerAdvice(new GameControllerAdvice()).build();
		gameRequest.setMaxPlayers(5);
	}

	@Test
	void findAvailableGames() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games")
								.header("X-Player", "player"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotHaveJsonPath());
	}

	@Test
	void createGame() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId("12613126");
		gameDetails.setStatus("WaitingForPlayers");
		when(gameService.createGame(eq("player"), any(NewGameRequest.class))).thenReturn(gameDetails);
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": 2\n" +
										"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value("12613126"))
				.andExpect(jsonPath("status").value("WaitingForPlayers"));
	}

	@Test
	void createGameFailedWithException() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": null\n" +
										"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"message\":\"Validation failed!\"," +
						"\"details\":[\"maxPlayers must not be null\"]}"));
	}

	@Test
	void suggestCharacter() throws Exception {
		doNothing().when(gameService).suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/1234/characters")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"character\": \" char\"\n" +
										"}"))
				.andExpect(status().isOk());
		verify(gameService, times(1)).suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));
	}

	@Test
	void suggestCharacterFailedWithException() throws Exception {
		doNothing().when(gameService).suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/1234/characters")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"character\":\"   \"\n" +
										"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"message\":\"Validation failed!\",\"details\":[\"must not be blank\"]}"));
	}

	@Test
	void enrollToGame() throws Exception {
		doNothing().when(gameService).enrollToGame(eq(id), eq("player"));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/games/{id}/players", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk());

		verify(gameService, times(1)).enrollToGame(eq(id), eq("player"));
	}

	@Test
	void findById() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);

		Optional<GameDetails> details = Optional.of(gameDetails);

		when(gameService.findByIdAndPlayer(gameDetails.getId(), "player")).thenReturn(details);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/games/{id}", gameDetails.getId())
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andExpect(jsonPath("id").value(details.get().getId()));

		verify(gameService, times(1)).findByIdAndPlayer(details.get().getId(), "player");
	}

	@Test
	void startGame() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);

		Optional<GameDetails> details = Optional.of(gameDetails);

		when(gameService.startGame(eq(details.get().getId()), eq("player"))).thenReturn(details);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/games/{id}", details.get().getId())
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andExpect(jsonPath("id").value(details.get().getId()));

		verify(gameService, times(1)).startGame(eq(details.get().getId()), eq("player"));
	}
}

