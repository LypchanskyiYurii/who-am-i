package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.TurnDetails;

import java.util.List;
import java.util.Optional;

public interface GameService {

	List<GameDetails> findAvailableGames(String player);

	GameDetails createGame(NewGameRequest gameRequest);

	SynchronousPlayer enrollToGame(String id, String player);

	Optional<GameDetails> findByIdAndPlayer(String id, String player);

	void suggestCharacter(String id, String player, CharacterSuggestion suggestion);

	Optional<GameDetails> startGame(String id, String player);

	void askQuestion(String gameId, String player, String message);

	Optional<TurnDetails> findTurnInfo(String id, String player);

	void submitGuess(String id, String player, String guess);

	void answerQuestion(String id, String player, String answer);
}
