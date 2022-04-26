package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

	private Map<String, String> playersCharacter = new HashMap<>();
	private List<Player> players;
	private List<String> availableCharacters;
	private Turn currentTurn;

	private final static String YES = "Yes";
	private final static String NO = "No";

	public RandomGame(List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<String>(availableCharacters);
		players = new ArrayList<>();
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();
		List<String> answers = new ArrayList<>();
		List<Thread> threads = new ArrayList<>();
		if (currentGuesser.isReadyForGuess()) {
			String guess = currentGuesser.getGuess();
			answers = currentTurn.getOtherPlayers().stream().parallel()
					.map(player -> player.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())))
					.collect(Collectors.toList());
//			currentTurn.getOtherPlayers().forEach(player -> {
//				Thread t = new Thread(
//						() -> answers.add(player.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName()))));
//				threads.add(t);
//				t.start();
//			});
//			while (!threads.isEmpty()) {
//				threads.removeIf(thread -> !thread.isAlive());
//			}
			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

			boolean win = positiveCount > negativeCount;

			if (win) {
				players.remove(currentGuesser);
			}
			return win;

		} else {
			String question = currentGuesser.getQuestion();
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())))
					.collect(Collectors.toList());
//			currentTurn.getOtherPlayers().forEach(player -> {
//				Thread t = new Thread(
//						() -> answers.add(player.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName()))));
//				threads.add(t);
//				t.start();
//			});
//			while (!threads.isEmpty()) {
//				threads.removeIf(thread -> !thread.isAlive());
//			}
			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
			return positiveCount > negativeCount;
		}

	}

	@Override
	public void assignCharacters() {
		players.stream().forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));

	}

	@Override
	public void initGame() {
		this.currentTurn = new TurnImpl(this.players);

	}

	@Override
	public boolean isFinished() {
		return players.size() == 1;
	}

	private String getRandomCharacter() {
		int randomPos = (int) (Math.random() * this.availableCharacters.size());
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

}