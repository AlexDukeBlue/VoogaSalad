package input_parse;

import backend.util.GameState;
import input_parse.InputDecoder.Type;

public class ParseMain {
	
	public static void main(String[] args) {
		GameState gameState = new TestGameState();
		Parser parser = new Parser(gameState);
		parser.parse(Type.CONSUMER, "(hello) -> {hello.endTurn();};");
		UserConsumer consumer = new UserConsumer();
		consumer.getConsumer().accept(gameState);
	}

}
