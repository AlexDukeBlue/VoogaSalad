package backend.game_engine;

public class Resultant {

	private ResultQuadPredicate result;
	private String nameOf;
	
	public Resultant(ResultQuadPredicate resultQuad, String name){
		result = resultQuad;
		nameOf = name;
	}
	
	public ResultQuadPredicate getResultQuad() {
		return result;
	}
	
	public String getNameOf() {
		return nameOf;
	}
}
