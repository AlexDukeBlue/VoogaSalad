boolean b = true;
for(backend.unit.Unit unit: team.getOwnedUnits(gameState.getGrid())){
	b = (unit.getMovePoints().getCurrentValue().intValue() == 0);
}
return b;