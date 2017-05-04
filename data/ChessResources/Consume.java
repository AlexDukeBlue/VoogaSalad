ArrayList<backend.unit.Unit> units = new ArrayList<>(gameState.getGrid().get(target.getLocation()).getOccupants());
units.forEach(e -> e.takeDamage(10000));
user.moveTo(gameState.getGrid().get(target.getLocation()),gameState);

return null;