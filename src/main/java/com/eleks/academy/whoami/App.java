package com.eleks.academy.whoami;

import java.io.IOException;
<<<<<<< HEAD
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
=======
>>>>>>> 86e2147a1480be5896307595e1a5943cdaceccfa
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eleks.academy.whoami.configuration.ContextConfig;
import com.eleks.academy.whoami.configuration.ServerProperties;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.Server;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {
<<<<<<< HEAD
		int players = readPlayersArg(args);

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		List<Socket> playerList = new ArrayList<>(players);
		try {
			for (int i = 0; i < players; i++) {
				var socket = server.waitForPlayer(game);
				playerList.add(socket);
				Thread parallelClientWorker = new Thread(() -> identifyPlayer(server, socket));
				parallelClientWorker.start();
				// Start a parallel thread to process a client
			}
			System.out.println(String.format("Got %d players. Starting a game.", players));

			boolean gameStatus = true;
			game.assignCharacters();
			game.initGame();
			while (gameStatus) {
				boolean turnResult = game.makeTurn();

				while (turnResult) {
					turnResult = game.makeTurn();
				}
				game.changeTurn();
				gameStatus = !game.isFinished();
			}
		} finally {
			server.stop();
		}
	}

	private static void identifyPlayer(ServerImpl server, Socket socket) {
		try {
			PrintWriter toClient = new PrintWriter(socket.getOutputStream());
			toClient.println("Please, name yourself.");
			toClient.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			var playerName = reader.readLine();
			synchronized (server) {
				server.addPlayer(new ClientPlayer(playerName, socket));
			}
		} catch (IOException e) {
			System.err.println("Identification of a client failed: " + e.getMessage());
		}
	}

	private static int readPlayersArg(String[] args) {
		if (args.length < 1) {
			return 2;
		} else {
			try {
				int players = Integer.parseInt(args[0]);
				if (players < 2) {
					return 2;
				} else if (players > 5) {
					return 5;
				} else {
					return players;
				}
			} catch (NumberFormatException e) {
				System.err.printf("Cannot parse number of players. Assuming 2. (%s)%n", e.getMessage());
				return 2;
			}
		}
=======
		ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
		ServerProperties properties = context.getBean(ServerProperties.class);
		Server server = context.getBean(Server.class);

		Game game = server.startGame();
		game.play();
>>>>>>> 86e2147a1480be5896307595e1a5943cdaceccfa
	}

}