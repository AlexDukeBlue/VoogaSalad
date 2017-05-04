boolean towersWin = false;
if(gameState.getTeamByName("Soldiers").getOwnedUnits
gameState.getGrid()).isEmpty()){
	towersWin = true;
}
if(team.getName().equals("Soldiers")){
	if(towersWin){
		return "LOSE";
	} else {
		return "NONE";
	}
}
if(team.getName().equals("Towers")){
	if(towersWin){
		return "WIN";
	} else {
		return "NONE";
	}
}
return "NONE";