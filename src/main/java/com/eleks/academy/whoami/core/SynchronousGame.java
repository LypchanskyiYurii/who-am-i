package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.util.List;
import java.util.Optional;

public interface SynchronousGame {

	Optional<SynchronousPlayer> findPlayer(String player);

	String getId();

	Optional<PlayerWithState> enrollToGame(String player);

	List<PlayerWithState> getPlayersInGame();

	String getStatus();

	boolean isAvailable();

	String getTurn();

	void askQuestion(String player, String message);

	void answerQuestion(String player, Answer answer);

	SynchronousGame start();

	Integer getPlayersCount();

	void deletePlayer(String player);

}
