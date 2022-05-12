package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class WaitingForPlayers extends AbstractGameState {

	private final Map<String, SynchronousPlayer> players;

	public WaitingForPlayers(int maxPlayers) {
		super(0, maxPlayers);
		this.players = new HashMap<>(maxPlayers);
	}

	private WaitingForPlayers(int maxPlayers, Map<String, SynchronousPlayer> players) {
		super(0, maxPlayers);
		this.players = players;
	}

	@Override
	public GameState next() {
		return new SuggestingCharacters(this.players);
	}

	@Override
	public GameState makeTurn(Answer answer) {
		Map<String, SynchronousPlayer> nextPlayers = new HashMap<>(this.players);
		if (nextPlayers.containsKey(answer.getPlayer())) {
			throw new GameException("Cannot enroll to the game");
		} else {
			nextPlayers.put(answer.getPlayer(), new PersistentPlayer(answer.getPlayer()));
		}
		if (players.size() == maxPlayers) {
			return new SuggestingCharacters(players);
		} else {
			return new WaitingForPlayers(maxPlayers, nextPlayers);
		}
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public int getPlayersInGame() {
		return this.players.size();
	}

}
