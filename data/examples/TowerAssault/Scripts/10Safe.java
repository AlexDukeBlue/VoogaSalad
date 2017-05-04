boolean soldiersWin = false;
for(backend.cell.Cell cell: gameState.getGrid().getCells().values()){
	if(cell.getTerrain().getName().equals("Castle")){
		if(cell.getOccupants().size() >= 10){
			soldiersWin = true;
		}
	}
}
if(team.getName().equals("Soldiers")){
	if(soldiersWin){
		return "WIN";
	} else {
		return "NONE";
	}
}
if(team.getName().equals("Towers")){
	if(soldiersWin){
		return "LOSE";
	} else {
		return "NONE";
	}
}
return "NONE";