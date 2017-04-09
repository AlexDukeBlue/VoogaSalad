import backend.util.GameState;
import backend.util.ImmutableGameState;
import backend.util.io.JSONSerializer;
import backend.util.io.JSONSerializer;
import util.net.ObservableServer;

import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * @author Created by th174 on 4/4/2017.
 */
public class VoogaServerMain {
	public static final int PORT = 10023;
	public static final int TIMEOUT = 20;

	public static void main(String[] args) throws Exception {
		//TODO
		JSONSerializer<ImmutableGameState> serializer = new JSONSerializer<>(GameState.class);
		ObservableServer<ImmutableGameState> voogaServer = new ObservableServer<>(new GameState(), PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		Executors.newSingleThreadExecutor().submit(voogaServer);
		System.out.println("Server started successfully...");
	}
}
