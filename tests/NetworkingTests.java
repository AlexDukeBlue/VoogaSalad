import backend.player.Player;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
import org.junit.Test;
import util.net.ObservableClient;
import util.net.ObservableServer;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * @author Created by th174 on 4/11/2017.
 */
public class NetworkingTests {
	public static final int PORT = 10023;
	public static final String HOST = ObservableClient.LOCALHOST;
	public static final int TIMEOUT = 20;
	public static final String CHATBOX = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n------------TEST GAME STATE CHAT LOG------------\n\n%s\n\n >>  ";
	public static final XMLSerializer<GameplayState> serializer = new XMLSerializer<>();
	public static final String name1 = System.getProperty("user.name") + "1";
	public static final String name2 = System.getProperty("user.name") + "2";
//		JSONSerializer<ImmutableGameState> serializer = new JSONSerializer<>(GameplayState.class);

	@Test
	public void testServerStart() throws Exception {
		XMLSerializer<GameplayState> serializer = new XMLSerializer<>();
//		JSONSerializer<ImmutableGameState> serializer = new JSONSerializer<>(GameState.class);
		ObservableServer<GameplayState> voogaServer = new ObservableServer<>(new GameplayState("test", null, "", ""), PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		Executors.newSingleThreadExecutor().submit(voogaServer);
		System.out.println("Server started successfully...");
	}

	@Test
	public void testClientStartAndAddPlayer() throws Exception {
		ObservableClient<GameplayState> client = new ObservableClient<>(HOST, PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		Executors.newSingleThreadExecutor().submit(client);
		Thread.sleep(1000);
		client.addListener(state -> assertTrue(state.getAllPlayers().stream().anyMatch(player -> player.getName().equals(name1))));
		client.addToOutbox(state -> {
			state.addPlayer(new Player(name1, "It's me!", ""));
			return state;
		});
	}

	@Test
	public void testMessaging() throws IOException, InterruptedException {
		ObservableClient<GameplayState> client = new ObservableClient<>(HOST, PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		Executors.newSingleThreadExecutor().submit(client);
		String msg = "testing testing";
		client.addToOutbox(state -> state.messageAll(msg, state.getPlayerByName(name2)));
		Thread.sleep(1000);
		client.addListener(state -> assertTrue(state.getPlayerByName(name1).getChatLog().stream().anyMatch(e -> e.getContent().contains(msg))));
		client.addListener(state -> assertTrue(state.getPlayerByName(name2).getChatLog().stream().anyMatch(e -> e.getContent().contains(msg))));
		client.addListener(state -> System.out.println(state.getAllPlayers().toString()));
		for (int i = 0; i < 10; i++) {
			client.addToOutbox(state -> state.messageAll(msg, state.getPlayerByName(name2)));
		}
	}
}
