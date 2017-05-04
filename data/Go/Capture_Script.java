//Go Capture Script
gameState.getActiveTeam().getOwnedUnits(gameState.getGrid()).stream()
		.filter(unit -> Objects.nonNull(unit.getCurrentCell()))
		.filter(unit -> (unit.getCurrentCell().getOccupants().size() > 1))
		.filter(un -> un.getLocation().get(0) != 5)
		.filter(uni -> uni.getLocation().get(1) != 5)
		.forEach(u -> u.getCurrentCell().removeOccupants(u.getCurrentCell().getOccupants().toArray(new backend.unit.Unit[0])));


return gameState;