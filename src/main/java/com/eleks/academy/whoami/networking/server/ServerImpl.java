package com.eleks.academy.whoami.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
<<<<<<< HEAD
import java.net.Socket;
=======
>>>>>>> 86e2147a1480be5896307595e1a5943cdaceccfa
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman", "Tor", "Gladiator", "Terminator");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
	private List<String> guessess = List.of("Batman", "Superman", "Tor", "Gladiator", "Terminator");

	private final ServerSocket serverSocket;
<<<<<<< HEAD
	private final List<Socket> openSockets = new ArrayList<>();
=======
	private final List<Player> clientPlayers;
	
	private final int players;
>>>>>>> 86e2147a1480be5896307595e1a5943cdaceccfa

	public ServerImpl(int port, int players) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.players = players;
		this.clientPlayers = new ArrayList<>(players);
	}

	@Override
	public Game startGame() throws IOException {
		RandomGame game = new RandomGame(clientPlayers ,characters);
		game.initGame();
		return game;
	}

	@Override
<<<<<<< HEAD
	public Socket waitForPlayer(Game game) throws IOException {
		Socket player = serverSocket.accept();
		openSockets.add(player);
		return player;
	}

	@Override
	public void addPlayer(Player player) {
		game.addPlayer(player);
		System.out.println("Player: " + player.getName() + " Connected to the game!");

=======
	@PostConstruct
	public void waitForPlayer() throws IOException {
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		for(int i = 0; i < players; i++) {
			ClientPlayer clientPlayer = new ClientPlayer(serverSocket.accept());
			clientPlayers.add(clientPlayer);
		}
		System.out.println(String.format("Got %d players. Starting a game.", players));
>>>>>>> 86e2147a1480be5896307595e1a5943cdaceccfa
	}

	@Override
	@PreDestroy
	public void stop() {
		for(Player player: clientPlayers) {
			try {
				player.close();
			} catch (Exception e) {
				System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
			}
		}
	}

	public void stop() {
		for (Socket s : openSockets) {
			try {
				s.close();
			} catch (IOException e) {
				System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
			}
		}
	}

}