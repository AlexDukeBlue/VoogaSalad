gameState.getActiveTeam().getOwnedUnits(gameState.getGrid()).stream()
															.filter(unit -> (unit.getCurrentCell().getOccupants().size() > 1))
															.filter(un -> !un.getTeam().equals(team))
															.forEach(u -> u.getCurrentCell().removeOccupants(u));


return gameState;
