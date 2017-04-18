package controller;

import backend.Model;
import backend.util.AuthoringGameState;

public class ModelGenerator implements Model {

	private CommunicationController controller;
	private AuthoringGameState gameState;
	
	public ModelGenerator(CommunicationController mController, AuthoringGameState mGameState) {
		this.controller=mController;
		this.gameState = mGameState;
	}
		
	private CommunicationController myController;

	public ModelGenerator(CommunicationController myController) {
		this.myController=myController;
		generateGameState();
	}

	public void generateGameState()
	{
		//GameRuleCreator. Pass in gameState
			//Translate/parse/etc
		//This is basically just XML/JSON
	}
	
	public void setGameState(AuthoringGameState mGameState)
	{
		this.gameState = mGameState;
	}
	
	@Override
	public AuthoringGameState getGameState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthoringGameState setGameState() {
		// TODO Auto-generated method stub
		return null;
	}

}
