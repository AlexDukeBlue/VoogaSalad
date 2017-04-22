package backend.game_engine;

public class Resultant {

	private ResultQuadPredicate result;
	private String nameOf;

	/**
	 * This constructor takes a ResultQuadPredicate which returns a Result(WIN, LOSE, TIE, NONE) and the name of the
	 * ResultQuadPredicate. This is effectively a wrap-around class that allows for the removal of a RQP.
	 *
	 * @param resultQuad
	 * @param name
	 */
	public Resultant(ResultQuadPredicate resultQuad, String name) {
		result = resultQuad;
		nameOf = name;
	}

	/**
	 * Returns the Resultant's ResultQuadPredicate.
	 *
	 * @return ResultQuadPredicate
	 */
	public ResultQuadPredicate getResultQuad() {
		return result;
	}

	/**
	 * Returns the name of the ResultQuadPredicate.
	 *
	 * @return String
	 */
	public String getNameOf() {
		return nameOf;
	}
}
