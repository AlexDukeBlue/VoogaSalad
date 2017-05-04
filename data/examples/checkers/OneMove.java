boolean o = (team.getOwnedUnits(gameState.getGrid())).stream().anyMatch(unit ->
		(unit.getUnitStat("Move Points").getCurrentValue()) ==
		((Integer) 0));

return o;