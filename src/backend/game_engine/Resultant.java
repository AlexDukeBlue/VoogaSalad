package backend.game_engine;

import backend.util.ImmutableVoogaObject;
import backend.util.VoogaEntity;

public class Resultant extends ImmutableVoogaObject<Resultant>{

	private ResultQuadPredicate result;

	/**
	 * This constructor takes a ResultQuadPredicate which returns a Result(WIN, LOSE, TIE, NONE) and the name of the
	 * ResultQuadPredicate. This is effectively a wrap-around class that allows for the removal of a RQP.
	 *
	 * @param resultQuad
	 * @param name
	 */
	public Resultant(ResultQuadPredicate resultQuad, String name, String description) {
		this(resultQuad, name, description, "");
	}
	
	public Resultant(ResultQuadPredicate resultQuad, String name, String description, String imgPath){
		super(name, description, imgPath);
		result = resultQuad;
	}

	/**
	 * Returns the Resultant's ResultQuadPredicate.
	 *
	 * @return ResultQuadPredicate
	 */
	public ResultQuadPredicate getResultQuad() {
		return result;
	}

	@Override
	public Resultant copy() {
		return new Resultant(result, getName(), getDescription(), getImgPath());
	}
}
