import backend.grid.GridPattern;
import backend.util.GameplayState;
import backend.util.io.XMLSerializer;
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
		@SuppressWarnings("unused")
		GridPattern pattern = GridPattern.HEXAGONAL_ADJACENT;
		XMLSerializer<GameplayState> serializer = new XMLSerializer<>();
//		JSONSerializer<ImmutableGameState> serializer = new JSONSerializer<>(GameState.class);
		ObservableServer<GameplayState> voogaServer = new ObservableServer<>(new GameplayState("test", null, "", ""), PORT, serializer, serializer, Duration.ofSeconds(TIMEOUT));
		Executors.newSingleThreadExecutor().submit(voogaServer);
		System.out.println("Server started successfully...");
	}
}
