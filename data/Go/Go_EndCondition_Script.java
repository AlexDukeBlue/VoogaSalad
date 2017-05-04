//End Conditions
//
Integer whiteScore = 0;
Integer blackScore = 0;

if (gameState.getGrid().get(5,5).getOccupants().size() == 2){
	for (backend.cell.Cell cell : gameState.getGrid().getCells().values()){
		for (backend.unit.Unit unit : cell.getOccupants()){
			if (unit.getName().startsWith("White Stone")){
				whiteScore++;
			}
			if (unit.getName().startsWith("Black Stone")){
				blackScore++;
			}
		}
	}
	
	if (team.getName().startsWith("White")){
		System.out.println("team is white");
		if (whiteScore >= blackScore){
			System.out.println("white wins");
			return "WIN";
		} else {
			System.out.println("black wins");
			return "LOSE";
		}
	} else {
		System.out.println("team is black");
		if (whiteScore < blackScore){
			System.out.println("black wins");
			return "WIN";
		} else {
			System.out.println("white wins");
			return "LOSE";
		}
	}
} else {
	return "NONE";
}